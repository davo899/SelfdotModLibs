package com.selfdot.libs.minecraft.midi;

import com.selfdot.libs.minecraft.midi.event.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Slf4j
public class MIDILoader {

    private static final String MIDI_DIRECTORY = "midis";

    @Getter
    private static final MIDILoader instance = new MIDILoader();
    private MIDILoader() { loadMidis(); }

    @Getter
	private final Map<String, MIDI> songBank = new HashMap<>();
	public void loadMidis() {
        try {
            File midiDirectory = new File(MIDI_DIRECTORY);
            Files.createDirectories(midiDirectory.toPath());
            String[] paths = midiDirectory.list();
            if (paths == null) return;
            Arrays.stream(paths).forEach(path ->
                songBank.put(path.substring(0, path.length() - 4), loadMidi(MIDI_DIRECTORY + "/" + path))
            );

        } catch (IOException e) {
            log.error("Could not load MIDI files:");
            log.error(e.getMessage());
        }
    }

	public List<Integer> loadMidiFile(String fileName) {
        List<Integer> res = new ArrayList<>();
		try {
			File file = new File(fileName);
            if (!file.exists()) return res;
            byte[] array = new byte[(int)file.length()];
            //noinspection ResultOfMethodCallIgnored,resource
            new FileInputStream(file).read(array);
            for (byte byt : array) res.add(Byte.toUnsignedInt(byt));
	        
		} catch (FileNotFoundException e) {
            log.error(fileName + " not found");
            
        } catch (IOException ioe) {
            log.error("Exception while reading " + fileName + ":");
            log.error(ioe.getMessage());
        }
		
		return res;
	}
	
	public String getBytes(int n, List<Integer> arr, int base){
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < n; i++) {
			if (base == 16) {
				String hexadecimal = Integer.toHexString(arr.remove(arr.size() - 1));
				if (hexadecimal.length() == 1) res.append("0");
				res.append(hexadecimal);
		  
			} else if (base == 2) {
				StringBuilder bits = new StringBuilder(Integer.toBinaryString(arr.remove(arr.size() - 1)));
				while (bits.length() < 8) bits.insert(0, "0");
				res.append(bits);
			}
		}
		return res.toString();
	}

	public int getVarLengthInt(List<Integer> bytesInDec) {
		StringBuilder binInt = new StringBuilder("0");
		boolean end = false;
		while (!end) {
			String byt = getBytes(1, bytesInDec, 2);
			binInt.append(byt.substring(1));
			end = byt.charAt(0) == '0';
		}
		return Integer.parseInt(binInt.toString(), 2);
	}
	
	public MIDI loadMidi(String fileName) {
		List<Integer> arr = loadMidiFile(fileName);
		List<Integer> bytesInDec = new ArrayList<>();
		
		for (Integer n : arr) bytesInDec.add(0, n);
		
		MIDI midi = new MIDI();
		while (bytesInDec.size() > 0) {
			String type = getBytes(4, bytesInDec, 16);
			
			if (type.equals("4d546864")) {
				getBytes(4, bytesInDec, 16);
				midi.format = Integer.parseInt(getBytes(2, bytesInDec, 16), 16);
				midi.numOfTracks = Integer.parseInt(getBytes(2, bytesInDec, 16), 16);
				
				String division = getBytes(2, bytesInDec, 2);
				if (division.charAt(0) == '0') {
					midi.deltaTime = Integer.parseInt(division.substring(1), 2);
				} else {
					midi.fps = -Integer.parseInt(division.substring(0, 9), 2);
					midi.deltaTime = Integer.parseInt(division.substring(9), 2);
				}
				
			} else if (type.equals("4d54726b")) {
				List<MIDIEvent> track = new ArrayList<>();
				String status;
				getBytes(4, bytesInDec, 16);
				
				while (true) {
					MIDIEvent event = null;
					int time = getVarLengthInt(bytesInDec);
					
					String nextByte = "0"+Integer.toHexString(bytesInDec.get(bytesInDec.size() - 1));
					if (nextByte.length() > 2) nextByte = nextByte.substring(nextByte.length() - 2);
					
					if (
                        Set.of('8', '9', 'a', 'b', 'c', 'd', 'e').contains(nextByte.charAt(0)) ||
                        Set.of("f0", "f7", "ff").contains(nextByte)
                    ) {
						status = getBytes(1, bytesInDec, 16);
					} else {
                        continue;
                    }

                    int statusFirst = Integer.parseInt(String.valueOf(status.charAt(1)), 16);
                    if (status.charAt(0) == '8') {
                        String data = getBytes(2, bytesInDec, 16);
						int key = Integer.parseInt(data.substring(0, 2), 16);
						int velocity = Integer.parseInt(data.substring(2), 16);
						
						event = new NoteOffEvent(time, statusFirst, key, velocity);

					} else if (status.charAt(0) == '9') {
                        String data = getBytes(2, bytesInDec, 16);
						int key = Integer.parseInt(data.substring(0, 2), 16);
						int velocity = Integer.parseInt(data.substring(2), 16);
                        event = velocity == 0 ?
                            new NoteOffEvent(time, statusFirst, key, velocity) :
                            new NoteOnEvent(time, statusFirst, key, velocity);
					}
					else if (status.charAt(0) == 'a') getBytes(2, bytesInDec, 16);
					else if (status.charAt(0) == 'b') getBytes(2, bytesInDec, 16);
					else if (status.charAt(0) == 'c') getBytes(1, bytesInDec, 16);
					else if (status.charAt(0) == 'd') getBytes(1, bytesInDec, 16);
					else if (status.charAt(0) == 'e') getBytes(2, bytesInDec, 16);
					else if (status.charAt(0) == 'f') {
						if (status.charAt(1) == '0') {
				            int dataLen = getVarLengthInt(bytesInDec);
				            getBytes(dataLen, bytesInDec, 16);
				            
						} else if(status.charAt(1) == '7') {
				            int dataLen = getVarLengthInt(bytesInDec);
				            getBytes(dataLen, bytesInDec, 16);
				            
						} else if(status.charAt(1) == 'f') {
							String metaCode = getBytes(1, bytesInDec, 16);
							if(metaCode.equals("2f")) {
								getBytes(1, bytesInDec, 16);
								break;
							}

				            int dataLen = getVarLengthInt(bytesInDec);
				            String data = getBytes(dataLen, bytesInDec, 16);
				            
				            if(metaCode.equals("51")) {
				            	event = new SetTempoEvent(time, Integer.parseInt(data, 16));
				            	
				            } else if(metaCode.equals("58")) {
				            	int ts1 = Integer.parseInt(data.substring(0, 2), 16);
				            	int ts2 = (int) Math.pow(2, Integer.parseInt(data.substring(2, 4), 16));
				            	int cpt = Integer.parseInt(data.substring(4, 6), 16);
				            	int npc = Integer.parseInt(data.substring(6), 16);
				            	event = new SetTimeSignatureEvent(time, ts1, ts2, cpt, npc);
				            }
						}
					}
					if (event == null) event = new MIDIEvent(time);
					track.add(event);
				}
				midi.tracks.add(track);
			} else break;
		}
		return midi;
	}

}
