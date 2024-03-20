package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CourceService {
    @Resource
    private CourceDao courceDao;

    @Resource
    private TeacherInfoDao teacherInfoDao;

    @Resource
    private ClassRoomDao classRoomDao;

    @Resource
    private ClassRoomService classRoomService;

    @Resource
    private ClassRoomReDao classRoomReDao;

    @Resource
    private StudentInfoDao studentInfoDao;

    @Resource
    private LogDao logDao;

    public PageInfo<Cource> selectAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cource> list = courceDao.selectAll();
        for (Cource cource : list) {
            if (ObjectUtil.isNotEmpty(cource.getTeacherId())) {
                TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(cource.getTeacherId());
                cource.setTeacherName(teacherInfo.getName());
            }
            if (cource.getWeek().equals("1")) {
                cource.setWeekName("星期一");
            }
            if (cource.getWeek().equals("2")) {
                cource.setWeekName("星期二");
            }
            if (cource.getWeek().equals("3")) {
                cource.setWeekName("星期三");
            }
            if (cource.getWeek().equals("4")) {
                cource.setWeekName("星期四");
            }
            if (cource.getWeek().equals("5")) {
                cource.setWeekName("星期五");
            }
            if (cource.getWeek().equals("6")) {
                cource.setWeekName("星期六");
            }
            if (cource.getWeek().equals("7")) {
                cource.setWeekName("星期日");
            }
        }
        return PageInfo.of(list);
    }

    public PageInfo<Cource> selectByName(String name, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cource> list = courceDao.selectByName(name);
        for (Cource cource : list) {
            if (ObjectUtil.isNotEmpty(cource.getTeacherId())) {
                TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(cource.getTeacherId());
                cource.setTeacherName(teacherInfo.getName());
            }
            if (cource.getWeek().equals("1")) {
                cource.setWeekName("星期一");
            }
            if (cource.getWeek().equals("2")) {
                cource.setWeekName("星期二");
            }
            if (cource.getWeek().equals("3")) {
                cource.setWeekName("星期三");
            }
            if (cource.getWeek().equals("4")) {
                cource.setWeekName("星期四");
            }
            if (cource.getWeek().equals("5")) {
                cource.setWeekName("星期五");
            }
            if (cource.getWeek().equals("6")) {
                cource.setWeekName("星期六");
            }
            if (cource.getWeek().equals("7")) {
                cource.setWeekName("星期日");
            }
        }
        return PageInfo.of(list);
    }

    public void add(Cource cource) {
        courceDao.insertSelective(cource);
    }

    public void update(Cource cource) {
        courceDao.updateByPrimaryKeySelective(cource);
    }

    public void delete(Long id) {
        Cource cource = courceDao.selectByPrimaryKey(id);
        if (cource.getState() == 1) {
            throw new CustomException("-1" , "有别的老师预选了这个课程，你不能通过这种方式删除");
        }
        courceDao.deleteByPrimaryKey(id);
    }

    public Object selectName(Cource cource) {
        List<Cource> cources = courceDao.selectName(cource.getName());
        for (Cource cource1 : cources) {
            if (cource.getId() == cource1.getId()) {
                continue;
            }
            if (cource1.getName().equals(cource.getName())) {
                return cources;
            }
        }
        return null;
    }

    //    新增时的判断
    public Object selectName2(String name) {
        List<Cource> cources = courceDao.selectName(name);
        return cources.size() == 0 ? null : 1;
    }

    public List<Cource> findAll() {
        return courceDao.selectAll();
    }

    public Date[] getWeekDay() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        Date[] dates = new Date[7];
        for (int i = 0; i < 7; i++) {
            dates[i] = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public void xuanjiao(Cource cource, Long teacherId) {
        TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(teacherId);
        String type = cource.getType();
        if (type.indexOf(teacherInfo.getType()) == -1) {
            throw new CustomException("-1", "当前老师的专业和课程所要求的专业要求不同，不能选教这个课程");
        }

        String courceId = teacherInfo.getCourceId();
        String[] split2 = courceId.split("&");
        Map<String , String> map = new HashMap<>();
        for (String s : split2) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || "-1".equals(s)) {
                continue;
            }
            Cource cource1 = courceDao.selectByPrimaryKey(Long.valueOf(s));
            map.put(cource1.getWeek() , cource1.getDate());
        }
        if (ObjectUtil.isNotEmpty(map.get(cource.getWeek()))) {
            if (map.get(cource.getWeek()).equals(cource.getDate())) {
                throw new CustomException("-1" , "你在课程的那个时间内有别的课程，所以不能再选别的课程");
            }
        }

        Log log = logDao.selectById(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你选教" + cource.getName() + "课程");
        logDao.updateByPrimaryKeySelective(log);

        List<ClassRoom> classRoomList = classRoomDao.selectName(cource.getClassroom());
        ClassRoom classRoom = classRoomList.get(0);
        String date = cource.getDate();
        String week = cource.getWeek();
        String[] split = date.split("and");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date[] weekDay = getWeekDay();
        String format = dateFormat.format(weekDay[Integer.valueOf(week) - 1]);
        String initTime = format + "-" + split[0];
        String destroyTime = format + "-" + split[1];
        teacherInfo.setCourceId(teacherInfo.getCourceId() + "&" + cource.getId());
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
        classRoom.setTeacherId(teacherId + "");
        classRoom.setState(teacherId + "");
        classRoom.setInitTime(initTime);
        classRoom.setDestroyTime(destroyTime);
        classRoomDao.updateByPrimaryKeySelective(classRoom);
        cource.setTeacherId(teacherId);
        cource.setState(1l);
        courceDao.updateByPrimaryKeySelective(cource);
    }


    public void agree(Cource cource) {
        String classroom = cource.getClassroom();
        List<ClassRoom> classRoomList = classRoomDao.selectName(classroom);
        ClassRoom classRoom = classRoomList.get(0);
        classRoom.setIsTrue(1l);
        ClassRoomRe classRoomRe = new ClassRoomRe();
        classRoomRe.setName(classRoom.getName());
        classRoomRe.setCapacity(classRoom.getCapacity());
        classRoomRe.setInitTime(classRoom.getInitTime());
        classRoomRe.setDestroyTime(classRoom.getDestroyTime());
        classRoomReDao.insertSelective(classRoomRe);
        classRoom.setClassroomRe(classRoomRe.getId().toString());
        classRoomDao.updateByPrimaryKeySelective(classRoom);
        TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(Long.valueOf(classRoom.getState()));

        Log log = logDao.selectById(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员同意你选教" + cource.getName() + "课程");
        logDao.updateByPrimaryKeySelective(log);

        teacherInfo.setClassroomRe(teacherInfo.getClassroomRe() + "&" + classRoomRe.getId());
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
        cource.setIsTrue(1l);

        courceDao.updateByPrimaryKeySelective(cource);
    }


    public void disagree(Cource cource) {
        String classroom = cource.getClassroom();
        List<ClassRoom> classRoomList = classRoomDao.selectName(classroom);
        ClassRoom classRoom = classRoomList.get(0);
        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setName(classRoom.getName());
        classRoom1.setCapacity(classRoom.getCapacity());
        classRoomDao.insertSelective(classRoom1);
        classRoomDao.deleteByPrimaryKey(classRoom.getId());
        Long teacherId = cource.getTeacherId();
        TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(teacherId);

        Log log = logDao.selectById(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员不同意你选教" + cource.getName() + "课程");
        logDao.updateByPrimaryKeySelective(log);

        String courceId = teacherInfo.getCourceId();
        String[] split = courceId.split("&");
        String str = "";
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || s.equals(cource.getId().toString())) {
                continue;
            }
            str += "&" + s;
        }
        teacherInfo.setCourceId(str);
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);

        Cource cource1 = new Cource();
        cource1.setName(cource.getName());
        cource1.setDate(cource.getDate());
        cource1.setWeek(cource.getWeek());
        cource1.setWeekDay(cource.getWeekDay());
        cource1.setType(cource.getType());
        cource1.setClassroom(cource.getClassroom());
        cource1.setCapacity(0l);
        courceDao.deleteByPrimaryKey(cource.getId());
        courceDao.insertSelective(cource1);
    }

    public void force(Cource cource) {
        throw new CustomException("-1" , "此功能涉及的数据太多，且没有什么技术含量，所以不实现了");
    }

    public void xuanke(Cource cource, Long studentId) {
        List<ClassRoom> classRoomList = classRoomDao.selectName(cource.getClassroom());
        ClassRoom classRoom = classRoomDao.selectByPrimaryKey(classRoomList.get(0).getId());
        StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(studentId);
        if (cource.getType().indexOf(studentInfo.getType()) == -1) {
            throw new CustomException("-1" , "你这个专业不能选择当前课程");
        }
        if (studentInfo.getCourceId().indexOf(cource.getId().toString()) != -1) {
            throw new CustomException("-1" , "你已经选过这门课程了，不能再选");
        }
        if (cource.getCapacity() == Long.valueOf(classRoom.getCapacity())) {
            throw new CustomException("-1" , "当前选课的人数已满，不允许再次选课");
        }

        Log log = logDao.selectById(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你选了" + cource.getName() + "课程");
        logDao.updateByPrimaryKeySelective(log);

        String courceId = studentInfo.getCourceId();
        String[] split = courceId.split("&");
        Map<String , String> map = new HashMap<>();
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || "-1".equals(s)) {
                continue;
            }
            Cource cource1 = courceDao.selectByPrimaryKey(Long.valueOf(s));
            map.put(cource1.getWeek() , cource1.getDate());
        }
        if (ObjectUtil.isNotEmpty(map.get(cource.getWeek()))) {
            if (map.get(cource.getWeek()).equals(cource.getDate())) {
                throw new CustomException("-1" , "你在课程的那个时间内有别的课程，所以不能再选别的课程");
            }
        }
        cource.setCapacity(cource.getCapacity() + 1l);
        courceDao.updateByPrimaryKeySelective(cource);
        studentInfo.setCourceId(studentInfo.getCourceId() + "&" + cource.getId());
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
    }

    public PageInfo<Cource> selectAllTe(Integer pageNum, Integer pageSize , TeacherInfo teacherInfo) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cource> list = courceDao.selectAll();
        List<Cource> list2 = new ArrayList<>();
        for (Cource cource : list) {
            if (cource.getWeek().equals("1")) {
                cource.setWeekName("星期一");
            }
            if (cource.getWeek().equals("2")) {
                cource.setWeekName("星期二");
            }
            if (cource.getWeek().equals("3")) {
                cource.setWeekName("星期三");
            }
            if (cource.getWeek().equals("4")) {
                cource.setWeekName("星期四");
            }
            if (cource.getWeek().equals("5")) {
                cource.setWeekName("星期五");
            }
            if (cource.getWeek().equals("6")) {
                cource.setWeekName("星期六");
            }
            if (cource.getWeek().equals("7")) {
                cource.setWeekName("星期日");
            }
            if (ObjectUtil.isNotEmpty(cource.getTeacherId()) && cource.getTeacherId() == teacherInfo.getId() && ObjectUtil.isNotEmpty(cource.getIsTrue())) {
                list2.add(cource);
            }
        }
        return PageInfo.of(list2);
    }

    public PageInfo<Cource> selectByNameTe(String name, Integer pageNum, Integer pageSize, TeacherInfo teacherInfo) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cource> list = courceDao.selectByName(name);
        List<Cource> list2 = new ArrayList<>();
        for (Cource cource : list) {
            if (cource.getWeek().equals("1")) {
                cource.setWeekName("星期一");
            }
            if (cource.getWeek().equals("2")) {
                cource.setWeekName("星期二");
            }
            if (cource.getWeek().equals("3")) {
                cource.setWeekName("星期三");
            }
            if (cource.getWeek().equals("4")) {
                cource.setWeekName("星期四");
            }
            if (cource.getWeek().equals("5")) {
                cource.setWeekName("星期五");
            }
            if (cource.getWeek().equals("6")) {
                cource.setWeekName("星期六");
            }
            if (cource.getWeek().equals("7")) {
                cource.setWeekName("星期日");
            }
            if (ObjectUtil.isNotEmpty(cource.getTeacherId()) && cource.getTeacherId() == teacherInfo.getId() && cource.getState() == 1) {
                list2.add(cource);
            }
        }
        return PageInfo.of(list2);
    }

    public TeacherInfo tuidingTe(TeacherInfo teacherInfo, Long id) {
        Cource cource = courceDao.selectByPrimaryKey(id);
        List<ClassRoom> classRoomList = classRoomDao.selectName(cource.getClassroom());
        classRoomService.tuidingTe(Long.valueOf(classRoomList.get(0).getClassroomRe()) , teacherInfo , false);
        String courceId = teacherInfo.getCourceId();
        String[] split = courceId.split("&");
        String str = "";
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || id == Long.valueOf(s)) {
                continue;
            }
            str += "&" + s;
        }
        teacherInfo.setCourceId(str);

        Log log = logDao.selectById(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你不教了" + cource.getName() + "课程");
        logDao.updateByPrimaryKeySelective(log);

        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
        List<StudentInfo> studentInfos = studentInfoDao.selectAll();
        for (StudentInfo studentInfo : studentInfos) {
            String courceId1 = studentInfo.getCourceId();
            String[] split1 = courceId1.split("&");
            String str2 = "";
            for (String s : split1) {
                if (ObjectUtil.isEmpty(s) || "null".equals(s) || cource.getId() == Long.valueOf(s)) {
                    continue;
                }
                str2 += "&" + s;
            }
            studentInfo.setCourceId(str2);

            String img = studentInfo.getImg();
            if (ObjectUtil.isNotEmpty(img)) {
                String[] split2 = img.split("and");
                String str23 = "";
                for (String s : split2) {
                    if (ObjectUtil.isEmpty(s) || "null".equals(s) || "".equals(s) || s.indexOf(id.toString()) != -1) {
                        continue;
                    }
                    str23 += s;
                }
                studentInfo.setImg(str23);
            }

            String score = studentInfo.getHomeworkscore();
            if (ObjectUtil.isNotEmpty(score)) {
                String[] split3 = score.split("and");
                String str3 = "";
                for (String s : split3) {
                    if (ObjectUtil.isEmpty(s) || "null".equals(s) || "".equals(s) || s.indexOf(id.toString()) != -1) {
                        continue;
                    }
                    str3 += s;
                }
                studentInfo.setHomeworkscore(str3);
            }

            studentInfoDao.updateByPrimaryKeySelective(studentInfo);
        }
        Cource cource1 = new Cource();
        cource1.setName(cource.getName());
        cource1.setDate(cource.getDate());
        cource1.setWeek(cource.getWeek());
        cource1.setWeekDay(cource.getWeekDay());
        cource1.setType(cource.getType());
        cource1.setClassroom(cource.getClassroom());
        cource1.setCapacity(0l);
        courceDao.deleteByPrimaryKey(id);
        courceDao.insertSelective(cource1);

        return teacherInfo;
    }

    public List<Cource> schoolTime(StudentInfo studentInfo) {
        String courceId = studentInfo.getCourceId();
        String[] split = courceId.split("&");
        List<Cource> list = new ArrayList<>();
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "-1".equals(s) || "null".equals(s)) {
                continue;
            }
            list.add(courceDao.selectByPrimaryKey(Long.valueOf(s)));
        }
        for (Cource cource : list) {
            cource.setTeacherName(teacherInfoDao.selectByPrimaryKey(cource.getTeacherId()).getName());
        }
        return list;
    }

    public void tuixuan(Long studentId, Cource cource) {
        StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(studentId);
        Long id = cource.getId();
        String courceId = studentInfo.getCourceId();
        if (ObjectUtil.isNotEmpty(courceId) && courceId.indexOf(id.toString()) == -1) {
            throw new CustomException("-1" , "你都没有选择当前课程，不可以退课");
        }
        String[] split = courceId.split("&");
        String str = "";
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || id == Long.valueOf(s)) {
                continue;
            }
            str += "&" + s;
        }
        studentInfo.setCourceId(str);

        Log log = logDao.selectById(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你退选了" + cource.getName() + "课程");
        logDao.updateByPrimaryKeySelective(log);

        String img = studentInfo.getImg();
        String[] split1 = img.split("and");
        String str2 = "";
        for (String s : split1) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || "".equals(s) || s.indexOf(id.toString()) != -1) {
                continue;
            }
            str2 += s;
        }
        studentInfo.setImg(str2);

        String score = studentInfo.getHomeworkscore();
        String[] split3 = score.split("and");
        String str3 = "";
        for (String s : split3) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s) || "".equals(s) || s.indexOf(id.toString()) != -1) {
                continue;
            }
            str3 += s;
        }
        studentInfo.setHomeworkscore(str3);

        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
        cource.setCapacity(cource.getCapacity() - 1l);
        courceDao.updateByPrimaryKeySelective(cource);
    }

    public void upload(Cource cource) {
        Long teacherId = cource.getTeacherId();
        Log log = logDao.selectById(teacherId + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你上传了" + cource.getName() + "课程的作业");
        logDao.updateByPrimaryKeySelective(log);
        courceDao.updateByPrimaryKeySelective(cource);
    }

    public PageInfo<Cource> selectAllSt(Integer pageNum, Integer pageSize , StudentInfo studentInfo) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cource> list = courceDao.selectAll();
        List<Cource> list2 = new ArrayList<>();
        String courceId = studentInfo.getCourceId();
        for (Cource cource : list) {
            if (cource.getWeek().equals("1")) {
                cource.setWeekName("星期一");
            }
            if (cource.getWeek().equals("2")) {
                cource.setWeekName("星期二");
            }
            if (cource.getWeek().equals("3")) {
                cource.setWeekName("星期三");
            }
            if (cource.getWeek().equals("4")) {
                cource.setWeekName("星期四");
            }
            if (cource.getWeek().equals("5")) {
                cource.setWeekName("星期五");
            }
            if (cource.getWeek().equals("6")) {
                cource.setWeekName("星期六");
            }
            if (cource.getWeek().equals("7")) {
                cource.setWeekName("星期日");
            }
            if (ObjectUtil.isNotEmpty(cource.getTeacherId()) && courceId.indexOf(cource.getId().toString()) != -1) {
                list2.add(cource);
            }
        }
        if (ObjectUtil.isNotEmpty(studentInfo.getHomeworkscore())) {
            String homeworkscore = studentInfo.getHomeworkscore();
            String[] split = homeworkscore.split("and");
            for (Cource cource : list2) {
                for (String s : split) {
                    if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                        continue;
                    }
                    if (s.indexOf(cource.getId().toString()) != -1) {
                        cource.setScore(s.split("&")[0]);
                    }
                }
            }
        }
        return PageInfo.of(list2);
    }

    public PageInfo<Cource> selectByNameSt(String name, Integer pageNum, Integer pageSize, StudentInfo studentInfo) {
        PageHelper.startPage(pageNum, pageSize);
        List<Cource> list = courceDao.selectByName(name);
        List<Cource> list2 = new ArrayList<>();
        String courceId = studentInfo.getCourceId();
        for (Cource cource : list) {
            if (cource.getWeek().equals("1")) {
                cource.setWeekName("星期一");
            }
            if (cource.getWeek().equals("2")) {
                cource.setWeekName("星期二");
            }
            if (cource.getWeek().equals("3")) {
                cource.setWeekName("星期三");
            }
            if (cource.getWeek().equals("4")) {
                cource.setWeekName("星期四");
            }
            if (cource.getWeek().equals("5")) {
                cource.setWeekName("星期五");
            }
            if (cource.getWeek().equals("6")) {
                cource.setWeekName("星期六");
            }
            if (cource.getWeek().equals("7")) {
                cource.setWeekName("星期日");
            }
            if (ObjectUtil.isNotEmpty(cource.getTeacherId()) && courceId.indexOf(cource.getId().toString()) != -1) {
                list2.add(cource);
            }
        }
        if (ObjectUtil.isNotEmpty(studentInfo.getHomeworkscore())) {
            String homeworkscore = studentInfo.getHomeworkscore();
            String[] split = homeworkscore.split("and");
            for (Cource cource : list2) {
                for (String s : split) {
                    if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                        continue;
                    }
                    if (s.indexOf(cource.getId().toString()) != -1) {
                        cource.setScore(s.split("&")[0]);
                    }
                }
            }
        }
        return PageInfo.of(list2);
    }

    public void uploadSt(StudentInfo studentInfo, String img , Long courceId) {
        if (ObjectUtil.isNotEmpty(studentInfo.getImg()) && studentInfo.getImg().indexOf(courceId.toString()) != -1) {
            throw new CustomException("-1" , "你已经上传过作业了，不能再次上传");
        }
        Cource cource = courceDao.selectByPrimaryKey(courceId);
        if (ObjectUtil.isEmpty(cource.getImg())) {
            throw new CustomException("-1" , "老师都还没有上传作业，你还不能提交");
        }
        Log log = logDao.selectById(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你提交了" + cource.getName() + "课程的作业");
        logDao.updateByPrimaryKeySelective(log);

        studentInfo.setImg(studentInfo.getImg() + "and" + img + "&" + courceId);
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
    }

    public List<StudentInfo> getHomeSt(Cource cource) {
        List<StudentInfo> list = new ArrayList<>();
        List<StudentInfo> studentInfos = studentInfoDao.selectAll();
        for (StudentInfo studentInfo : studentInfos) {
            if (studentInfo.getCourceId().indexOf(cource.getId().toString()) != -1) {
                list.add(studentInfo);
            }
        }
        for (StudentInfo studentInfo : list) {
            String img = studentInfo.getImg();
            if (ObjectUtil.isEmpty(img)) {
                continue;
            }
            String[] split = img.split("and");
            String str = "";
            for (String s : split) {
                if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                    continue;
                }
                if (s.indexOf(cource.getId().toString()) != -1) {
                    str = s;
                    break;
                }
            }
            studentInfo.setImg(str.split("&")[0]);
        }
        return list;
    }

    public void getScore(StudentInfo studentInfo, String score , String img) {
        StudentInfo studentInfo1 = studentInfoDao.selectByPrimaryKey(studentInfo.getId());
        String img1 = studentInfo1.getImg();
        if (ObjectUtil.isEmpty(img1)) {
            throw new CustomException("-1" , "这位同学还没有上传作业，不能打分");
        }
        String[] split = img1.split("and");
        String str = "";
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            if (s.indexOf(img) != -1) {
                str = s;
                break;
            }
        }
        if (ObjectUtil.isNotEmpty(str) && ObjectUtil.isNotEmpty(studentInfo1.getHomeworkscore()) && studentInfo1.getHomeworkscore().indexOf(str.split("&")[1]) != -1) {
            throw new CustomException("-1" , "你已经给当前学生给过分数了，不能再给");
        }

        Cource cource = courceDao.selectByPrimaryKey(Long.valueOf(str.split("&")[1]));
        TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(cource.getTeacherId());

        Log log = logDao.selectById(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + teacherInfo.getName() +  "老师给你的" + cource.getName() + "课程的作业打了" + score + "的分数");
        logDao.updateByPrimaryKeySelective(log);

        Log log2 = logDao.selectById(teacherInfo.getId() + "&teacher");
        log2.setContent(log2.getContent() + "and" + dateTime.format(formatter) + "&" + "你给" + studentInfo.getName() + "学生的" + cource.getName() + "课程的作业打了" + score + "的分数");
        logDao.updateByPrimaryKeySelective(log2);

        studentInfo1.setHomeworkscore(studentInfo1.getHomeworkscore() + "and" + score + "&" + str.split("&")[1]);
        studentInfoDao.updateByPrimaryKeySelective(studentInfo1);
    }
}
