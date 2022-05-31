package com.huanghe.common;

import com.huanghe.exception.FileOperException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 通用的File操作类
 */
public class FileCommonUtil {
    /**
     * 功能描述: 获取文件
     *
     * @since 2020年3月20日
     * @param filePath 文件路径
     * @return 文件
     * @throws FileOperException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    public static File getFile(String filePath) throws FileOperException {
        if (StringUtils.isEmpty(filePath)) {
            throw new FileOperException("Get file object error, file path is empty.");
        }
        return FileUtils.getFile(filePath);
    }

    /**
     * 功能描述: 获取文件流
     *
     * @since 2020年3月20日
     * @param file 文件
     * @return 文件流
     * @throws FileOperException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    public static InputStream openInputStream(File file) throws FileOperException {
        if (file == null) {
            throw new FileOperException("Open file input stream failed, the input parameter is null.");
        }

        try {
            return FileUtils.openInputStream(file);
        } catch (IOException e) {
            throw new FileOperException("Open file input stream error.", e);
        }
    }

    /**
     * 功能描述: 文件是否存在
     *
     * @param file 文件
     * @return 返回是否存在
     * @see [类、类#方法、类#成员]
     */
    public static boolean isFileExist(File file) {
        if (file == null) {
            return false;
        }
        return isExist(file) && file.isFile();
    }

    /**
     * 功能描述: 目录是否存在
     *
     * @param file 文件
     * @return 返回是否存在
     * @see [类、类#方法、类#成员]
     */
    public static boolean isDirExist(File file) {
        if (file == null) {
            return false;
        }

        return isExist(file) && file.isDirectory();
    }

    /**
     * 功能描述: 目录或文件是否存在
     *
     * @param file 文件
     * @return 返回是否存在
     * @see [类、类#方法、类#成员]
     */
    public static boolean isExist(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }


    /**
     * 功能描述:创建目录
     *
     * @param file 文件目录
     * @throws FileOperException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    public static void mkdirsWithPermission(File file) throws FileOperException {
        if (file == null) {
            throw new FileOperException("Make directory failed, the input file object is null.");
        }

        if (!file.exists()) {
            if (!file.mkdirs()) {
                throw new FileOperException("Make directory error.");
            }
            // 设置目录权限
            setDirectoryPermission(file);
        }
    }

    /**
     * 功能描述: 设置目录权限（750）
     *
     * @param file 目录对象
     * @throws FileOperException 设置权限异常
     * @see [类、类#方法、类#成员]
     */
    public static void setDirectoryPermission(File file) throws FileOperException {
        if (file == null) {
            throw new FileOperException("Set directory perssion failed, the input file object is null.");
        }
        Set<PosixFilePermission> filePerms = getDirectoryPermissionSet();
        setUserPermission(file, filePerms);
    }

    /**
     * 功能描述: 设置文件权限（640）
     *
     * @param file 文件对象
     * @throws FileOperException 设置权限异常
     * @see [类、类#方法、类#成员]
     */
    public static void setFilePermission(File file) throws FileOperException {
        if (file == null) {
            throw new FileOperException("Set file perssion failed, the input file object is null.");
        }

        Set<PosixFilePermission> filePerms = getFilePermissionSet();
        setUserPermission(file, filePerms);
    }

    private static Set<PosixFilePermission> getFilePermissionSet() {
        Set<PosixFilePermission> filePerms = new HashSet<>();
        filePerms.add(PosixFilePermission.OWNER_READ);
        filePerms.add(PosixFilePermission.OWNER_WRITE);
        filePerms.add(PosixFilePermission.GROUP_READ);
        return filePerms;
    }

    private static Set<PosixFilePermission> getDirectoryPermissionSet() {
        Set<PosixFilePermission> filePerms = new HashSet<>();
        filePerms.add(PosixFilePermission.OWNER_READ);
        filePerms.add(PosixFilePermission.OWNER_WRITE);
        filePerms.add(PosixFilePermission.OWNER_EXECUTE);
        filePerms.add(PosixFilePermission.GROUP_READ);
        filePerms.add(PosixFilePermission.GROUP_EXECUTE);
        return filePerms;
    }

    private static void setUserPermission(File dirFile, Set<PosixFilePermission> perms) throws FileOperException {
        try {
            // 使用getCanonicalPath而非getAbsolutePath获取文件路径
            Path path = Paths.get(dirFile.getCanonicalPath());
            Files.setPosixFilePermissions(path, perms);
        } catch (UnsupportedOperationException e) {
            throw new FileOperException("Set user permission unsupported, message:{}", e);
        } catch (IOException e) {
            throw new FileOperException("Set user permission error.", e);
        }
    }

    /**
     * 功能描述:单个流关闭
     *
     * @param closeable 待关闭的流
     * @throws FileOperException 文件操作异常
     * @see [类、类#方法、类#成员]
     */
    public static void closeStream(Closeable closeable) throws FileOperException{
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new FileOperException("Close stream error.", e);
            }
        }
    }

    /**
     * 功能描述:多个流关闭
     *
     * @param closeables 待关闭的流
     * @see [类、类#方法、类#成员]
     */
    public static void closeStreams(List<Closeable> closeables) {
        if (closeables == null) {
            return;
        }

        for (Closeable closeable : closeables) {
            closeStream(closeable);
        }
    }
}
