/*
*Copyright (c) 2020,2022, HFU and/or its affiliates. All rights reserved.
*
*
*/


package com.training.vo;

import java.io.FileOutputStream;
import java.io.IOException;

/**
*
*
*
*
*<br>author: MingChih Hong
*@since 11.0<br>
*TODO:
*
*/
public class UploadTask implements Runnable {
    private FileOutputStream fos;
    private byte[] buffer;
    private int length;

    public UploadTask(FileOutputStream fos, byte[] buffer, int length) {
        this.fos = fos;
        this.buffer = buffer;
        this.length = length;
    }

    @Override
    public void run() {
        try {
            // 在線程中寫入圖片數據
            fos.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}