package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.dao.*;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassRoomService {
    @Resource
    private ClassRoomDao classRoomDao;

    @Resource
    private StudentInfoDao studentInfoDao;

    @Resource
    private TeacherInfoDao teacherInfoDao;

    @Resource
    private ClassRoomReDao classRoomReDao;

    @Resource
    private LogDao logDao;

    @Resource
    private CourceDao courceDao;

    public PageInfo<ClassRoom> findAll(Integer pageNum , Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<ClassRoom> list = classRoomDao.selectAll();
        List<ClassRoom> list1 = new ArrayList<>();
        List<Long> id = new ArrayList<>();
        for (ClassRoom classRoom : list) {
            if (ObjectUtil.isNotEmpty(classRoom.getStudentId())) {
                list1 = classRoomDao.selectAllSt();
                for (ClassRoom room : list1) {
                    for (ClassRoom classRoom1 : list) {
                        if (room.getName().equals(classRoom1.getName())) {
                            list.remove(classRoom1);
                            id.add(classRoom1.getId());
                            break;
                        }
                    }
                }
                for (ClassRoom room : list1) {
                    for (Long aLong : id) {
                        if (room.getId() == aLong) {
                            list.add(room);
                        }
                    }
                }
                break;
            }
        }
        id = new ArrayList<>();
        for (ClassRoom classRoom : list) {
            if (ObjectUtil.isNotEmpty(classRoom.getTeacherId())) {
                list1 = classRoomDao.selectAllTe();
                for (ClassRoom room : list1) {
                    for (ClassRoom classRoom1 : list) {
                        if (room.getName().equals(classRoom1.getName())) {
                            list.remove(classRoom1);
                            id.add(classRoom1.getId());
                            break;
                        }
                    }
                }
                for (ClassRoom room : list1) {
                    for (Long aLong : id) {
                        if (room.getId() == aLong) {
                            list.add(room);
                        }
                    }
                }
                break;
            }
        }
        return PageInfo.of(list);
    }


    public PageInfo<ClassRoomRe> findAllSt(Integer pageNum , Integer pageSize , StudentInfo studentInfo) {
        PageHelper.startPage(pageNum , pageSize);
        String classroomRe = studentInfo.getClassroomRe();
        if (ObjectUtil.isEmpty(classroomRe)) {
            return PageInfo.of(new ArrayList<>());
        }
        String[] split = classroomRe.split("&");
        List<ClassRoomRe> list = new ArrayList<>();
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            list.add(classRoomReDao.selectByPrimaryKey(Long.valueOf(s)));
        }
        return PageInfo.of(list);
    }


    public PageInfo<ClassRoomRe> findAllTe(Integer pageNum , Integer pageSize , TeacherInfo teacherInfo) {
        PageHelper.startPage(pageNum , pageSize);
        String classroomRe = teacherInfo.getClassroomRe();
        List<ClassRoomRe> list = new ArrayList<>();
        if (ObjectUtil.isEmpty(classroomRe)) {
            return PageInfo.of(list);
        }
        String[] split = classroomRe.split("&");
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            list.add(classRoomReDao.selectByPrimaryKey(Long.valueOf(s)));
        }
        return PageInfo.of(list);
    }


    public ClassRoom findById(Long aLong) {
        return classRoomDao.selectByPrimaryKey(aLong);
    }

    public PageInfo<ClassRoom> selectByName(String name , Integer pageNum , Integer pageSize) {
        PageHelper.startPage(pageNum , pageSize);
        List<ClassRoom> list = classRoomDao.selectByName(name);
        List<ClassRoom> list1 = new ArrayList<>();
        List<Long> id = new ArrayList<>();
        for (ClassRoom classRoom : list) {
            if (ObjectUtil.isNotEmpty(classRoom.getStudentId())) {
                list1 = classRoomDao.selectAllSt();
                for (ClassRoom room : list1) {
                    for (ClassRoom classRoom1 : list) {
                        if (room.getName().equals(classRoom1.getName())) {
                            list.remove(classRoom1);
                            id.add(classRoom1.getId());
                            break;
                        }
                    }
                }
                for (ClassRoom room : list1) {
                    for (Long aLong : id) {
                        if (room.getId() == aLong) {
                            list.add(room);
                        }
                    }
                }
                break;
            }
            if (ObjectUtil.isNotEmpty(classRoom.getTeacherId())) {
                list1 = classRoomDao.selectAllTe();
                for (ClassRoom room : list1) {
                    for (ClassRoom classRoom1 : list) {
                        if (room.getName().equals(classRoom1.getName())) {
                            list.remove(classRoom1);
                            id.add(classRoom1.getId());
                            break;
                        }
                    }
                }
                for (ClassRoom room : list1) {
                    for (Long aLong : id) {
                        if (room.getId() == aLong) {
                            list.add(room);
                        }
                    }
                }
                break;
            }
        }
        return PageInfo.of(list);
    }


    public PageInfo<ClassRoomRe> selectByNameSt(String name , Integer pageNum , Integer pageSize , StudentInfo studentInfo) {
        PageHelper.startPage(pageNum , pageSize);
        String classroomRe = studentInfo.getClassroomRe();
        String[] split = classroomRe.split("&");
        List<ClassRoomRe> list = new ArrayList<>();
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            ClassRoomRe classRoomRe2 = classRoomReDao.selectByPrimaryKey(Long.valueOf(s));
            if (classRoomRe2.getName().indexOf(name) != -1) {
                list.add(classRoomReDao.selectByPrimaryKey(Long.valueOf(s)));
            }
        }
        return PageInfo.of(list);
    }


    public PageInfo<ClassRoomRe> selectByNameTe(String name , Integer pageNum , Integer pageSize , TeacherInfo teacherInfo) {
        PageHelper.startPage(pageNum , pageSize);
        String classroomRe = teacherInfo.getClassroomRe();
        String[] split = classroomRe.split("&");
        List<ClassRoomRe> list = new ArrayList<>();
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            ClassRoomRe classRoomRe2 = classRoomReDao.selectByPrimaryKey(Long.valueOf(s));
            if (classRoomRe2.getName().indexOf(name) != -1) {
                list.add(classRoomReDao.selectByPrimaryKey(Long.valueOf(s)));
            }
        }
        return PageInfo.of(list);
    }

    public void add(ClassRoom classRoom) {
        classRoomDao.insertSelective(classRoom);
    }

    public void update(ClassRoom classRoom) {
        classRoomDao.updateByPrimaryKeySelective(classRoom);
    }

    public void delete(Long id) {
        ClassRoom classRoom = classRoomDao.selectByPrimaryKey(id);
        if (ObjectUtil.isNotEmpty(classRoom.getTeacherId())) {
            throw new CustomException("-1" , "这个教室是被老师预约的，可能有什么重要的事情，不能删除");
        }
        classRoomDao.deleteByPrimaryKey(id);
    }

    public Object selectName(ClassRoom classRoom) {
        List<ClassRoom> classRoomList = classRoomDao.selectName(classRoom.getName());
        boolean is = false;
        for (ClassRoom classRoom1 : classRoomList) {
            if (classRoom1.getName().equals(classRoom.getName()) && classRoom1.getId() == classRoom.getId()) {
                is = true;
            }
        }
        if (classRoomList.size() == 1 && is) {
            return null;
        }
        return classRoomList;
    }

    public Object selectName2(String name) {
        List<ClassRoom> classRoomList = classRoomDao.selectName(name);
        return classRoomList.size() == 0 ? null : 1;
    }

    public List<ClassRoom> findAll() {
        return classRoomDao.selectAll();
    }

    public void re(ClassRoom classRoom, Long userId , String level) {
        classRoom.setState(userId + "");
        if (level.equals("student")) {
            classRoom.setStudentId(userId + "");
            Log log = logDao.selectById(userId + "&student");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你预约了" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        } else {
            classRoom.setTeacherId(userId + "");
            Log log = logDao.selectById(userId + "&teacher");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你预约了" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        }
        classRoomDao.updateByPrimaryKeySelective(classRoom);
    }

    public void agree(ClassRoom classRoom) {
        classRoom.setIsTrue(1l);
        ClassRoomRe classRoomRe = new ClassRoomRe();
        classRoomRe.setName(classRoom.getName());
        classRoomRe.setCapacity(classRoom.getCapacity());
        classRoomRe.setInitTime(classRoom.getInitTime());
        classRoomRe.setDestroyTime(classRoom.getDestroyTime());
        classRoomReDao.insertSelective(classRoomRe);
        classRoom.setClassroomRe(classRoomRe.getId().toString());
        classRoomDao.updateByPrimaryKeySelective(classRoom);
        if (ObjectUtil.isNotEmpty(classRoom.getStudentId())) {
            StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(Long.valueOf(classRoom.getState()));
            studentInfo.setClassroomRe(studentInfo.getClassroomRe() + "&" + classRoomRe.getId());
            studentInfoDao.updateByPrimaryKeySelective(studentInfo);
            Log log = logDao.selectById(classRoom.getStudentId() + "&student");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员同意你预约" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        } else {
            TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(Long.valueOf(classRoom.getState()));
            teacherInfo.setClassroomRe(teacherInfo.getClassroomRe() + "&" + classRoomRe.getId());
            teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
            Log log = logDao.selectById(classRoom.getTeacherId() + "&teacher");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员同意你预约" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        }
    }

    public void disagree(ClassRoom classRoom) {
        if (ObjectUtil.isNotEmpty(classRoom.getStudentId())) {
            Log log = logDao.selectById(classRoom.getStudentId() + "&student");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员不同意你预约" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        } else {
            Log log = logDao.selectById(classRoom.getTeacherId() + "&teacher");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员不同意你预约" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        }

        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setName(classRoom.getName());
        classRoom1.setCapacity(classRoom.getCapacity());
        classRoomDao.insertSelective(classRoom1);
        classRoomDao.deleteByPrimaryKey(classRoom.getId());
    }


    public void forcere(ClassRoom classRoom) {

        List<Cource> list = courceDao.selectAll();
        for (Cource cource : list) {
            if (ObjectUtil.isNotEmpty(cource.getClassroom()) && cource.getClassroom().equals(classRoom.getName())) {
                throw new CustomException("-1" , "当前教室是被用来作为上课的教室的，所以要强制删除也去课程那里删除吧！");
            }
        }

        String name = classRoom.getName();
        if (ObjectUtil.isNotEmpty(classRoom.getStudentId())) {
            String studentId = classRoom.getStudentId();
            StudentInfo studentInfo = studentInfoDao.selectByPrimaryKey(studentId);
            String classroomRe = studentInfo.getClassroomRe();
            String[] split = classroomRe.split("&");
            String str = "";
            for (String s : split) {
                if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                    continue;
                }
                if (name.equals(classRoomReDao.selectByPrimaryKey(Long.valueOf(s)).getName())) {
                    classRoomReDao.deleteByPrimaryKey(Long.valueOf(s));
                    continue;
                }
                str += "&" + s;
            }
            studentInfo.setClassroomRe(str);
            studentInfoDao.updateByPrimaryKeySelective(studentInfo);

            Log log = logDao.selectById(classRoom.getStudentId() + "&student");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员强制删除了你预约的" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        } else {
            String teacherId = classRoom.getTeacherId();
            TeacherInfo teacherInfo = teacherInfoDao.selectByPrimaryKey(teacherId);
            String classroomRe = teacherInfo.getClassroomRe();
            String[] split = classroomRe.split("&");
            String str = "";
            for (String s : split) {
                if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                    continue;
                }
                if (name.equals(classRoomReDao.selectByPrimaryKey(Long.valueOf(s)).getName())) {
                    classRoomReDao.deleteByPrimaryKey(Long.valueOf(s));
                    continue;
                }
                str += "&" + s;
            }
            teacherInfo.setClassroomRe(str);
            teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);

            Log log = logDao.selectById(classRoom.getTeacherId() + "&teacher");
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "管理员强制删除了你预约的" + classRoom.getName() + "教室");
            logDao.updateByPrimaryKeySelective(log);
        }
        ClassRoom classRoom1 = new ClassRoom();
        classRoom1.setName(classRoom.getName());
        classRoom1.setCapacity(classRoom.getCapacity());
        classRoomDao.insertSelective(classRoom1);
        classRoomDao.deleteByPrimaryKey(classRoom.getId());
    }

    public StudentInfo tuidingSt(Long id, StudentInfo studentInfo) {
        ClassRoom classRoom1 = classRoomDao.selectByClassRe(id.toString());

        Log log = logDao.selectById(studentInfo.getId() + "&student");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你退订" + classRoom1.getName() + "教室");
        logDao.updateByPrimaryKeySelective(log);

        String classroomRe1 = classRoom1.getClassroomRe();
        classRoomReDao.deleteByPrimaryKey(Long.valueOf(classroomRe1));
        String classroomRe = studentInfo.getClassroomRe();
        String[] split = classroomRe.split("&");
        String str = "";
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            if (Long.valueOf(classroomRe1) == Long.valueOf(s)) {
                continue;
            }
            str += "&" + s;
        }
        studentInfo.setClassroomRe(str);
        studentInfoDao.updateByPrimaryKeySelective(studentInfo);
        List<ClassRoom> classRoomList = classRoomDao.selectAll();
        ClassRoom c = new ClassRoom();
        for (ClassRoom classRoom : classRoomList) {
            if (ObjectUtil.isNotEmpty(classRoom.getStudentId()) && studentInfo.getId() == Long.valueOf(classRoom.getStudentId())){
                c.setName(classRoom.getName());
                c.setCapacity(classRoom.getCapacity());
                classRoomDao.deleteByPrimaryKey(classRoom.getId());
                classRoomDao.insertSelective(c);
                break;
            }
        }
        return studentInfo;
    }

    public TeacherInfo tuidingTe(Long id, TeacherInfo teacherInfo , boolean is) {
        ClassRoom classRoom1 = classRoomDao.selectByClassRe(id.toString());

        if (is) {
            List<Cource> list = courceDao.selectAll();
            for (Cource cource : list) {
                if (ObjectUtil.isNotEmpty(cource.getClassroom()) && cource.getClassroom().indexOf(classRoom1.getName()) != -1) {
                    throw new CustomException("-1" , "当前教室是被用来做上课教室的，所以请到课程那里退订");
                }
            }
        }

        Log log = logDao.selectById(teacherInfo.getId() + "&teacher");
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        log.setContent(log.getContent() + "and" + dateTime.format(formatter) + "&" + "你退订" + classRoom1.getName() + "教室");
        logDao.updateByPrimaryKeySelective(log);

        String classroomRe1 = classRoom1.getClassroomRe();
        classRoomReDao.deleteByPrimaryKey(Long.valueOf(classroomRe1));
        String classroomRe = teacherInfo.getClassroomRe();
        String[] split = classroomRe.split("&");
        String str = "";
        for (String s : split) {
            if (ObjectUtil.isEmpty(s) || "null".equals(s)) {
                continue;
            }
            if (Long.valueOf(classroomRe1) == Long.valueOf(s)) {
                continue;
            }
            str += "&" + s;
        }
        teacherInfo.setClassroomRe(str);
        teacherInfoDao.updateByPrimaryKeySelective(teacherInfo);
        List<ClassRoom> classRoomList = classRoomDao.selectAll();
        ClassRoom c = new ClassRoom();
        for (ClassRoom classRoom : classRoomList) {
            if (ObjectUtil.isNotEmpty(classRoom.getTeacherId()) && teacherInfo.getId() == Long.valueOf(classRoom.getTeacherId())){
                c.setName(classRoom.getName());
                c.setCapacity(classRoom.getCapacity());
                classRoomDao.deleteByPrimaryKey(classRoom.getId());
                classRoomDao.insertSelective(c);
                break;
            }
        }
        return teacherInfo;
    }


    public List<ClassRoom> getClassGiveCource() {
        List<ClassRoom> classRoomList = classRoomDao.selectAll();
        List<ClassRoom> classRoomList2 = new ArrayList<>();
        for (ClassRoom classRoom : classRoomList) {
            if (ObjectUtil.isEmpty(classRoom.getState())) {
                classRoomList2.add(classRoom);
            }
        }
        return classRoomList2;
    }
}
