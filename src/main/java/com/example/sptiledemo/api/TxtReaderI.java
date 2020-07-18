/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.sptiledemo.api;

import com.example.sptiledemo.exp.DataRegexException;

import java.util.List;

/**
 *
 * @author Administrator
 */
public interface TxtReaderI {

    List<String> readStr(String fileName, String charset)throws DataRegexException, DataRegexException;
    Object[][] readObj(String fileName, String charset, Object[] objs)throws DataRegexException ;
    List<String> readStr(String fileName, String charset, String regex)throws DataRegexException ;
    
}
