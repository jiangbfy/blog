package com.blog.controller;

import com.blog.model.ResultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/server")
public class ServerController {
    private static final Integer OSHI_WAIT_SECOND = 1000;
    @GetMapping("/state")
    public ResultResponse<Map<String, Object>> state(){
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        CentralProcessor processor = hardware.getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        String cpuUse = new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu));

        GlobalMemory memory = hardware.getMemory();
        long totalByte = memory.getTotal();
        long acaliableByte = memory.getAvailable();
        String memUse = new DecimalFormat("#.##%").format(1.0 - 1.0 * acaliableByte / totalByte);

        FileSystem fileSystem = operatingSystem.getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        long totalSpace = 0;
        long avaSpace = 0;
        for (OSFileStore fs : fsArray) {
            totalSpace = totalSpace + fs.getTotalSpace();
            avaSpace = avaSpace + fs.getUsableSpace();
        }
        String spaceUse = new DecimalFormat("#.##%").format(1.0 - 1.0 * avaSpace / totalSpace);

        Map<String, Object> map = new HashMap<>();
        map.put("cpuUse", cpuUse);
        map.put("memUse", memUse);
        map.put("spaceUse", spaceUse);
        map.put("delay", "100ms");
        map.put("bw", "4Mbps");
        return ResultResponse.success("success", map);
    }
}
