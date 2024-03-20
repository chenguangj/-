package com.example.service;

import com.example.dao.NoticeDao;
import com.example.entity.Notice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class NoticeService {
    @Resource
    private NoticeDao noticeDao;

    public Notice getNotice(String name) {
        List<Notice> notices = noticeDao.selectAll();
        Notice noticeReal = null;
        for (Notice notice : notices) {
            if (name.equals(notice.getModule())) {
                noticeReal = notice;
            }
        }
        return noticeReal;
    }

    public void update(Notice notice) {
        noticeDao.updateByPrimaryKeySelective(notice);
    }

    public Notice getAllNotice(String module) {
        Notice notice = new Notice();
        List<Notice> notices = noticeDao.selectAll();
        for (Notice notice1 : notices) {
            if (notice1.getModule().equals(module)) {
                notice = notice1;
                break;
            }
        }
        return notice;
    }
}
