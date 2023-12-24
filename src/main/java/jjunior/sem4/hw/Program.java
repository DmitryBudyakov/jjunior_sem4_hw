package jjunior.sem4.hw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Program {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "password";
    private static final String DATABASE = "schoolDB";
    private static final String TABLE = "courses";
    private static final String coursesTableSQL = "id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), duration INT";

    public static void main(String[] args) throws SQLException {

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Создать базу данных " + DATABASE);
            System.out.println("2. Создать таблицу " + TABLE);
            System.out.println("3. Добавить тестовые записи в таблицу");
            System.out.println("4. Добавить запись в таблицу");
            System.out.println("5. Посмотреть все записи в таблице");
            System.out.println("6. Изменить запись");
            System.out.println("7. Удалить запись из таблицы");
            System.out.println("8. Выйти");
            System.out.print("Выберите действие: ");

            String select = scanner.nextLine();
            System.out.println();

            switch (select) {
                case "1":
                    // создание базы (через JDBC)
                    DbOperation.createDatabase(connection, DATABASE);
                    System.out.println("База данных " + DATABASE + " создана");
                    System.out.println();
                    break;
                case "2":
                    // создание таблицы (через JDBC)
                    DbOperation.createTable(connection, DATABASE, TABLE, coursesTableSQL);
                    System.out.println("Таблица " + DATABASE + "." + TABLE + " создана");
                    System.out.println();
                    break;
                case "3":
                    // добавление данных в таблицу (через Hibernate)
                    System.out.println("3. Добавление тестовых записей в таблицу");
                    addCoursesExample(factory, Course.exampleCourses);
                    System.out.println("Done!");
                    System.out.println();
                    break;
                case "4":
                    System.out.println("4. Добавление записи в таблицу");
                    System.out.print("Введите название дисциплины: ");
                    String title = scanner.nextLine();
                    System.out.print("Введите продолжительность: ");
                    int duration = Integer.parseInt(scanner.nextLine());
                    addCourse(factory, new Course(title, duration));
                    System.out.println("Done!");
                    System.out.println();
                    break;
                case "5":
                    System.out.println("5. Просмотр всех записей в таблице");
                    List<Course> coursesList = getAllCourses(factory, "Course");
                    if (coursesList.isEmpty()) {
                        System.out.println("В таблице нет записей");
                    } else {
                        for (Course c : coursesList) {
                            System.out.println(c);
                        }
                    }
                    System.out.println("Done!");
                    System.out.println();
                    break;
                case "6":
                    System.out.println("6. Изменение записи");
                    System.out.print("Введите номер записи для изменения: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println("Текущее состояние:");
                    Course course = getCourseById(factory, id);
                    System.out.println(course);

                    System.out.print("Введите новое название курса: ");
                    String newTitle = scanner.nextLine();
                    if (!newTitle.isEmpty())
                        course.setCourseTitle(newTitle);

                    System.out.print("Введите новую продолжительность курса: ");
                    String newDuration = scanner.nextLine();
                    if (!newDuration.isEmpty())
                        course.setCourseDuration(Integer.parseInt(newDuration));

                    updateCourseById(factory, course, id);

                    System.out.println("Done!");
                    System.out.println();
                    break;
                case "7":
                    System.out.println("7. Удаление записи по id");
                    System.out.print("Введите id записи, которую надо удалить: ");
                    int courseId = Integer.parseInt(scanner.nextLine());
                    deleteEntryById(factory, courseId);
                    System.out.println("Done!");
                    System.out.println();
                    break;
                case "8":
                    System.out.println("Пока!");
                    scanner.close();
                    factory.close();
                    System.exit(0);
                default:
                    System.out.println("Ошибка ввода. Повторите ввод.");
                    break;

            }
        }
    }

    /**
     * Получение всех записей из таблицы
     *
     * @param factory
     * @return
     */
    public static List<Course> getAllCourses(SessionFactory factory, String tableClass) {
        String querySQL = String.format("from %s", tableClass);
        Session session = factory.getCurrentSession();
        session.beginTransaction();

        List<Course> courseList = session.createQuery(querySQL).getResultList();

        session.getTransaction().commit();
        session.close();

        return courseList;
    }

    /**
     * Добавление тестовых записей в таблицу
     *
     * @param factory
     * @param coursesExampleList тестовый список дисциплин
     */
    public static void addCoursesExample(SessionFactory factory, List<Course> coursesExampleList) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        for (Course course : coursesExampleList) {
            session.persist(course);
        }
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Удаление дисциплины из таблицы по id
     *
     * @param factory
     * @param id
     */
    public static void deleteEntryById(SessionFactory factory, int id) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        session.remove(course);
        session.getTransaction().commit();
        session.close();
    }

    /**
     * Добавление дисциплины
     *
     * @param factory
     * @param course
     */
    public static void addCourse(SessionFactory factory, Course course) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.persist(course);
        session.getTransaction().commit();
        session.close();
    }


    /**
     * Возвращает дисциплину по id
     *
     * @param factory
     * @param id
     * @return
     */
    public static Course getCourseById(SessionFactory factory, int id) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        session.close();
        return course;
    }


    /**
     * Обновление данных по дисциплине
     * @param factory
     * @param updatedCourse
     * @param id
     */
    public static void updateCourseById(SessionFactory factory, Course updatedCourse, int id) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Course course = session.get(Course.class, id);
        course.setCourseTitle(updatedCourse.getCourseTitle());
        course.setCourseDuration(updatedCourse.getCourseDuration());
        session.update(course);
        session.getTransaction().commit();
        session.close();
    }

}
