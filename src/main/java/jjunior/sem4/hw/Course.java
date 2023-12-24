package jjunior.sem4.hw;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    public static final List<Course> exampleCourses = getCoursesExampleList();

    /**
     * Список дисциплин для тестирования
     * @return список дисциплин
     */
    private static List<Course> getCoursesExampleList() {
        List<Course> list = new ArrayList<>();
        Course course1 = new Course("Информатика", 3);
        Course course2 = new Course("Математика", 11);
        Course course3 = new Course("ИЗО", 4);
        Course course4 = new Course("Физкультура", 11);
        list.add(course1);
        list.add(course2);
        list.add(course3);
        list.add(course4);
        return list;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int courseId;

    @Column(name = "title")
    private String courseTitle;

    @Column(name = "duration")
    private int courseDuration;

    public Course() {
    }

    public Course(String courseTitle, int courseDuration) {
        this.courseTitle = courseTitle;
        this.courseDuration = courseDuration;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(int courseDuration) {
        this.courseDuration = courseDuration;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "courseId=" + courseId +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseDuration=" + courseDuration +
                '}';
    }


}
