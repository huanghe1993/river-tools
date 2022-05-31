package com.huanghe.zip;

import com.huanghe.common.FileCommonUtil;
import com.huanghe.enums.FileTypeEnum;
import com.huanghe.exception.FileZipException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件压缩解压工具类
 */
public final class FileZip {
    private static final int BUFFER = 1024;

    // max size of unzipped data, 100MB
    private static final long TOOBIG = 21990232555520L;

    // max number of files
    private static final int TOOMANY = 10240;

    /**
     * 功能描述: 解压zip
     *
     * @param zipFilePath zip文件路径
     * @param targetDir   加压目录
     * @throws FileZipException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    public static void decompressZip(String zipFilePath, String targetDir) throws FileZipException {
        decompressZip(zipFilePath, targetDir, StandardCharsets.UTF_8);
    }

    /**
     * 功能描述: 解压zip
     *
     * @param zipFilePath zip文件路径
     * @param targetDir   加压目录
     * @param charset     字符编码
     * @throws FileZipException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    public static void decompressZip(String zipFilePath, String targetDir, Charset charset)
            throws FileZipException {
        File zipFile = FileCommonUtil.getFile(zipFilePath);
        String fileName = zipFile.getName();
        // 校验待解压的文件是否为zip格式，是否是否存在
        if (!isEqualFileType(fileName.toLowerCase(Locale.ROOT), FileTypeEnum.ZIP.getValue())
                || !FileCommonUtil.isFileExist(zipFile)) {
            throw new FileZipException("Decompress zip file failed, the package is not zip or the zip file does not exist.");
        }
        // 解压的输出目录
        File targetDirFile = FileCommonUtil.getFile(targetDir);
        if (!FileCommonUtil.isDirExist(targetDirFile)) {
            // 创建解压目录
            FileCommonUtil.mkdirsWithPermission(targetDirFile);
        }

        InputStream fis = null;
        BufferedInputStream bis = null;
        ZipInputStream zis = null;
        try {
            fis = FileCommonUtil.openInputStream(zipFile);
            bis = new BufferedInputStream(fis);
            zis = new ZipInputStream(bis, charset);
            decompressZipFile(zis, targetDir);
        } catch (FileZipException e) {
            throw new FileZipException(e.getMessage(), e);
        } finally {
            FileCommonUtil.closeStreams(Arrays.asList(zis, bis, fis));
        }
    }

    private static void decompressZipFile(ZipInputStream zis, String targetDir) throws FileZipException {
        int entries = 0;
        long total = 0L;
        ZipEntry entry = null;
        try {
            while ((entry = zis.getNextEntry()) != null) {
                if (!sanitzeFileName(entry.getName(), targetDir)) {
                    throw new FileZipException("decompressZipFile: the filePath is unsafe!");
                }

                File tempFile = FileCommonUtil.getFile(targetDir + File.separator + entry.getName());

                // 创建目录
                if (!entry.isDirectory()) {
                    entries++;
                    total = decompressZipEntry(tempFile, total, zis);

                    if (entries > TOOMANY) {
                        throw new FileZipException("Too many files to unzip.");
                    }
                    if (total > TOOBIG) {
                        throw new FileZipException("File being unzipped is too big.");
                    }
                } else {
                    FileCommonUtil.mkdirsWithPermission(tempFile);
                }
            }
        } catch (IOException e) {
            throw new FileZipException("Get zip next entry error.", e);
        }
    }

    private static long decompressZipEntry(File tempFile, long total, ZipInputStream zis) throws FileZipException {
        FileOutputStream fos = null;
        BufferedOutputStream dest = null;
        long currLen = total;
        try {
            fos = FileUtils.openOutputStream(tempFile);
            dest = new BufferedOutputStream(fos, BUFFER);
            int count = -1;
            byte[] data = new byte[BUFFER];
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                currLen += count;
                // 当前写入的大小不能大于最大限制
                if (currLen > TOOBIG) {
                    throw new FileZipException(
                            "Decompress zip file failed, current decompress size has exceed the limit.");
                }
                dest.write(data, 0, count);
            }
            dest.flush();
            // 设置文件权限
            FileCommonUtil.setFilePermission(tempFile);
        } catch (IOException e) {
            throw new FileZipException("Decompress zip file failed.", e);
        } finally {
            FileCommonUtil.closeStreams(Arrays.asList(dest, fos));
        }

        return currLen;
    }

    /**
     * 文件名
     *
     * @param entryName   文件名
     * @param intendedDir 数据保存目录
     * @return 是否合法
     * @throws FileZipException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    private static Boolean sanitzeFileName(String entryName, String intendedDir) throws FileZipException {
        try {
            File file = FileUtils.getFile(intendedDir, entryName);
            String canonicalPath = file.getCanonicalPath();
            File iD = FileUtils.getFile(intendedDir);
            String canonicalID = iD.getCanonicalPath();
            if (canonicalPath.startsWith(canonicalID)) {
                return true;
            }
        } catch (IOException e) {
            throw new FileZipException("Sanitze file name error.", e);
        }
        return false;
    }


    private static boolean isEqualFileType(String fileName, String desiredType) {
        return fileName.endsWith(desiredType);
    }
}
