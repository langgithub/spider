package com.champion.spider.dfs.rmi;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;

import java.io.IOException;
import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by root on 2017/9/5.
 */
public class SpiderServiceImpl  extends UnicastRemoteObject implements SpiderService {

    public SpiderServiceImpl() throws RemoteException {
    }

    @Override
    public long start(String path) {
        Long PID=0L;
        try {
            Process exec = Runtime.getRuntime().exec(path);
            PID = getPID(exec);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PID;
    }

    @Override
    public void stop(Long pid) {
        try {
            Runtime.getRuntime().exec("taskill " + pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示当前机器的所有进程
     */
    public static void showTaskList() {

        try {
            Process process = Runtime.getRuntime().exec("taskList");
            Scanner in = new Scanner(process.getInputStream());
            int count = 0;
            while (in.hasNextLine()) {
                count++;
                System.out.println(count + ":" + in.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取进程id
     * @param process
     * @return
     */
    private static Long getPID(Process process){
        Long PID=0L;
        try {
            Field f =process.getClass().getDeclaredField("handle");
            f.setAccessible(true);
            long handl =f.getLong(process);
            Kernel32 kernel = Kernel32.INSTANCE;
            WinNT.HANDLE handle = new WinNT.HANDLE();
            handle.setPointer(Pointer.createConstant(handl));
            int ret =kernel.GetProcessId(handle);
            PID =Long.valueOf(ret);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return PID;
    }


    /**
     * 杀死进程树
     * @param process
     */
    private static void killProcessTree(Process process)
    {
        try {
            Field f =process.getClass().getDeclaredField("handle");
            f.setAccessible(true);
            long handl =f.getLong(process);
            Kernel32 kernel = Kernel32.INSTANCE;
            WinNT.HANDLE handle = new WinNT.HANDLE();
            handle.setPointer(Pointer.createConstant(handl));
            int ret =kernel.GetProcessId(handle);
            Long PID =Long.valueOf(ret);
            String cmd =getKillProcessTreeCmd(PID);
            Runtime rt =Runtime.getRuntime();
            Process killPrcess = rt.exec(cmd);
            killPrcess.waitFor();
            killPrcess.destroy();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private static String getKillProcessTreeCmd(Long Pid)
    {
        String result = "";
        if(Pid !=null)
            result ="cmd.exe /c taskkill /PID "+Pid+" /F /T ";
        return result;
    }

    /**
     * 根据pid 杀进程树
     * @param PID
     */
    public static void killProcessTreeByPid(Long PID)
    {
        try {
            String cmd =getKillProcessTreeCmd(PID);
            Runtime rt =Runtime.getRuntime();
            Process killPrcess = rt.exec(cmd);
            killPrcess.waitFor();
            killPrcess.destroy();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
