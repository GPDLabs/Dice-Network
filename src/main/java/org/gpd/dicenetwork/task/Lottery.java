package org.gpd.dicenetwork.task;

import org.gpd.dicenetwork.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Lottery {
    private Map<String, byte[]> hashes;
    private byte[] lotteryHash;
    private String winner;
    private byte[] winnerHash;


    public Lottery() {
        this.hashes = new HashMap<>();
    }

    public void addHash(String key, String hash) {
        hashes.put(key, StringUtils.hexStringToByteArray(hash));
    }

    public boolean lottery() {
        if (hashes.isEmpty()) {
            return false;
        }

        byte[] hash = new byte[32];
        for (String key : hashes.keySet()) {
            for (int i = 0; i < 32; i++) {
                hash[i] ^= hashes.get(key)[i];
            }
        }
        lotteryHash = hash;

        for (String key : hashes.keySet()) {
            for (int i = 0; i < 32; i++) {
                hashes.get(key)[i] ^= hash[i];
            }
        }

        List<Map.Entry<String, byte[]>> sortedEntries = new ArrayList<>(hashes.entrySet());
        sortedEntries.sort((e1, e2) -> {
            for (int i = 0; i < 32; i++) {
                int cmp = Byte.compare(e1.getValue()[i], e2.getValue()[i]);
                if (cmp != 0) {
                    return cmp;
                }
            }
            return 0;
        });

        winner = sortedEntries.get(0).getKey();

        hashes.clear();
        return true;
    }

    public String getWinner() {
        return winner;
    }

    public void removeWinner() {
    	winner = null;
    }

    public byte[] getWinnerHash() {
        return winnerHash;
    }

    public void removeWinnerHash() {
        winnerHash = null;
    }

    public byte[] getLotteryHash() {
    	return lotteryHash;
    }

}
