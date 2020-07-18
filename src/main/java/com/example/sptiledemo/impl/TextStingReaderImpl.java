/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sptiledemo.impl;


import com.example.sptiledemo.api.TxtReaderI;
import com.example.sptiledemo.exp.DataRegexException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class TextStingReaderImpl implements TxtReaderI {

    @Override
    public List<String> readStr(String fileName, String charset) throws DataRegexException {
        return readStr(fileName,charset,"/,");
    }

    @Override
    public Object[][] readObj(String fileName, String charset, Object[] objs) throws DataRegexException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<String> readStr(String fileName, String charset, String regex) throws DataRegexException {
        File file = new File(fileName);
        //检查文件是否存在
        if (!file.exists()) {
            System.err.println("Can not find file:" + fileName);
            return null;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            String line = null;
            List<String> ll = new ArrayList<>();
            int count = 0;
            while ((line = br.readLine()) != null) {
//                String[] linetxt = line.split(regex,-1);
//                if (count == 0) {
//                    count = linetxt.length;
//                } else {
//                    if (linetxt.length != count) {
//                        System.out.println(count+"   "+linetxt.length+"  "+line);
//                        throw new DataRegexException("data regex Error,please check regex separator!");
//
//                    }
//                }
//                for (int i=0;i<linetxt.length;i++){
                    ll.add(line);
//                }
            }
            return ll;
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(TextStingReaderImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TextStingReaderImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(TextStingReaderImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

}
