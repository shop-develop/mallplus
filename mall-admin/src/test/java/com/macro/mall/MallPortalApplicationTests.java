package com.macro.mall;

import com.zscat.mallplus.MallAdminApplication;
import com.zscat.mallplus.sys.entity.SysAdminLog;
import com.zscat.mallplus.sys.mapper.SysAdminLogMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootTest(classes = MallAdminApplication.class)
@RunWith(SpringRunner.class)
@Log4j2
public class MallPortalApplicationTests {

    @Resource
    SysAdminLogMapper sysAdminLogMapper;

    @Test
    public void contextLoads() {
        String ids ="1,2,3,4";
        List<SysAdminLog> ll =  sysAdminLogMapper.selectBatchIds(Arrays.asList(ids.split(",")));
        for (SysAdminLog log : ll){
            System.out.println(log.getMethod());
        }
    }
    @Test
    public void delete() {

        List<SysAdminLog> dd = new ArrayList<>();
        SysAdminLog q = new SysAdminLog();
        q.setId(6L);q.setCreateTime(new Date());
        dd.add(q);

        SysAdminLog q1 = new SysAdminLog();
        q1.setId(7L);q1.setCreateTime(new Date());
        dd.add(q1);

        SysAdminLog q2 = new SysAdminLog();
        q2.setId(8L);q2.setCreateTime(new Date());
        dd.add(q2);
        List<SysAdminLog> ll1 =  sysAdminLogMapper.selectBatchIds(dd);
        for (SysAdminLog log1 : ll1){
            System.out.println(log1.getMethod());
        }
         sysAdminLogMapper.deleteBatchIds(dd);
        List<SysAdminLog> ll =  sysAdminLogMapper.selectBatchIds(dd);
        for (SysAdminLog log : ll){
            System.out.println(log.getMethod());
        }
    }
}
