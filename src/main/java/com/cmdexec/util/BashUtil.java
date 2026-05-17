package com.cmdexec.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BashUtil {
    // 执行Linux bash命令
    public static String runBash(String cmd) {
        StringBuilder result = new StringBuilder();
        try {
            Process process = new ProcessBuilder("/bin/bash", "-c", cmd)
                    .redirectErrorStream(true) // 错误输出合并到标准输出
                    .start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
            );

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            result.append("执行异常：").append(e.getMessage());
        }
        return result.toString().isEmpty() ? "命令执行无输出" : result.toString();
    }
}

