package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.LogDao;
import com.example.entity.Log;
import com.example.entity.LogFen;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class LogService {
    @Resource
    private LogDao logDao;

    public Log getLog(Long id , String level) {
        Log log = logDao.selectById(id + "&" + level);
        return log;
    }

    public List<LogFen> fen(Log log) {
        List<LogFen> list = new ArrayList<>();
        if (ObjectUtil.isEmpty(log.getContent())) {
            return list;
        }
        String content = log.getContent();
        String[] split = content.split("and");
        List<String> spl = new ArrayList<>();
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            spl.add(s);
        }
        for (String s : spl) {
            String[] split1 = s.split("&");
            LogFen logFen = new LogFen();
            logFen.setTime(split1[0]);
            logFen.setContent(split1[1]);
            list.add(logFen);
        }
        return list;
    }
}
