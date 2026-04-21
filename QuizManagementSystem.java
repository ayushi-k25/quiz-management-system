import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class QuizManagementSystem extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final String LOGIN_SCREEN = "Login";
    private final String ADMIN_SCREEN = "Admin";
    private final String STUDENT_SCREEN = "Student";

    public QuizManagementSystem() {
        UIManager.put("Panel.background", new Color(245, 250, 255));
        UIManager.put("Button.background", new Color(200, 230, 255));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));

        setTitle("Quiz Management System - MySQL");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPanel(), LOGIN_SCREEN);
        mainPanel.add(new AdminPanel(), ADMIN_SCREEN);
        mainPanel.add(new StudentPanel(), STUDENT_SCREEN);
        mainPanel.add(manageQuestionPanel(), "ManageQuestions");
        mainPanel.add(viewResultPanel(), "ViewResults");
        mainPanel.add(manageQuizPanel(), "ManageQuiz");
        mainPanel.add(createQuizPanel(), "CreateQuiz");
        mainPanel.add(new CoursePanel(), "Courses");
        mainPanel.add(new MyCoursesPanel(), "MyCourses");
        mainPanel.add(addCoursePanel(), "AddCourse");

        add(mainPanel);
    }

    // ================= DATABASE =================
    static class DBConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/quizmanage";
        private static final String USER = "root";   // change if needed
        private static final String PASS = "AyushiRoot@123";   // change if needed

        public static Connection getConnection() throws Exception {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        }
    }

    // ================= LOGIN DAO =================
    static class User {
        int id;
        String username, role;

        User(int id, String username, String role) {
            this.id = id;
            this.username = username;
            this.role = role;
        }
    }

    static class UserSession {
        static User currentUser;
    }

    static class UserDAO {
        public static User login(String u, String p) {
    try (Connection con = DBConnection.getConnection()) {

        System.out.println("Connected to DB!");

        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, u);
        ps.setString(2, p);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Login SUCCESS");
            return new User(rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("role"));
        } else {
            System.out.println("No matching user found");
        }

    } catch (Exception e) {
        System.out.println("ERROR:");
        e.printStackTrace();   // 🔥 THIS WILL SHOW REAL PROBLEM
    }
    return null;
}
    }

    // ================= LOGIN PANEL =================
    class LoginPanel extends JPanel {
        LoginPanel() {
            setLayout(new GridBagLayout());
            setBackground(new Color(240, 248, 255));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel title = new JLabel("Quiz Management System");
            title.setFont(new Font("Segoe UI", Font.BOLD, 26));
            title.setForeground(new Color(70, 130, 180));

            JTextField user = new JTextField(15);
            JPasswordField pass = new JPasswordField(15);

            JButton login = new JButton("Login");

            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            add(title, gbc);

            gbc.gridwidth = 1; gbc.gridy = 1;
            add(new JLabel("Username:"), gbc);
            gbc.gridx = 1; add(user, gbc);

            gbc.gridx = 0; gbc.gridy = 2;
            add(new JLabel("Password:"), gbc);
            gbc.gridx = 1; add(pass, gbc);

            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
            add(login, gbc);

            login.addActionListener(e -> {
                User u = UserDAO.login(user.getText().trim(), new String(pass.getPassword()).trim());

                if (u != null) {
                    UserSession.currentUser = u;
                    if (u.role.equals("ADMIN"))
                        cardLayout.show(mainPanel, ADMIN_SCREEN);
                    else
                        cardLayout.show(mainPanel, STUDENT_SCREEN);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Login");
                }
            });
        }
    }

    // ================= ADMIN PANEL =================
    class AdminPanel extends JPanel {
    AdminPanel() {
        setLayout(new BorderLayout());

        JLabel header = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        add(header, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(3,1,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(50,200,50,200));

        JButton quizBtn = new JButton("Manage Quizzes");
        JButton questionBtn = new JButton("Manage Questions");
        JButton resultBtn = new JButton("View Results");
        JButton createQuizBtn = new JButton("Create Quiz");
        JButton addCourseBtn = new JButton("Add Course");


        quizBtn.setFocusPainted(false);
        questionBtn.setFocusPainted(false);
        resultBtn.setFocusPainted(false);
        createQuizBtn.setFocusPainted(false);
        addCourseBtn.setFocusPainted(false);

        quizBtn.setPreferredSize(new Dimension(200, 40));
        questionBtn.setPreferredSize(new Dimension(200, 40));
        resultBtn.setPreferredSize(new Dimension(200, 40));
        createQuizBtn.setPreferredSize(new Dimension(200, 40));
        addCourseBtn.setPreferredSize(new Dimension(400, 40));

        panel.add(quizBtn);
        panel.add(questionBtn);
        panel.add(resultBtn);
        panel.add(createQuizBtn);
        panel.add(addCourseBtn);


        add(panel, BorderLayout.CENTER);

        // 🔥 BUTTON ACTIONS
        quizBtn.addActionListener(e -> 
        cardLayout.show(mainPanel, "ManageQuiz")
        );
        questionBtn.addActionListener(e -> 
            cardLayout.show(mainPanel, "ManageQuestions")
        );

        resultBtn.addActionListener(e -> 
            cardLayout.show(mainPanel, "ViewResults")
        );

        createQuizBtn.addActionListener(e ->
            cardLayout.show(mainPanel, "CreateQuiz")
        );

        addCourseBtn.addActionListener(e ->
            cardLayout.show(mainPanel, "AddCourse")
        );

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            UserSession.currentUser = null;
            cardLayout.show(mainPanel, LOGIN_SCREEN);
        });

        JPanel bottom = new JPanel();
        bottom.add(logout);
        add(bottom, BorderLayout.SOUTH);
    }
}


void showLeaderboard() {

    JPanel panel = new JPanel(new BorderLayout());

    String[] cols = {"Rank", "Student", "Quiz", "Score"};
    JTable table = new JTable();

    try (Connection con = DBConnection.getConnection()) {

        String sql = "SELECT u.username, q.title, r.score " +
                     "FROM results r " +
                     "JOIN users u ON r.user_id = u.user_id " +
                     "JOIN quizzes q ON r.quiz_id = q.quiz_id " +
                     "ORDER BY r.score DESC";

        ResultSet rs = con.createStatement().executeQuery(sql);

        java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();

        int rank = 1;
        while (rs.next()) {
            list.add(new Object[]{
                    rank++,
                    rs.getString("username"),
                    rs.getString("title"),
                    rs.getInt("score")
            });
        }

        Object[][] data = new Object[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

    } catch (Exception e) {
        e.printStackTrace();
    }

    JButton backBtn = new JButton("⬅ Back");

    backBtn.addActionListener(e -> {
        cardLayout.show(mainPanel, STUDENT_SCREEN);
    });

    panel.add(new JScrollPane(table), BorderLayout.CENTER);
    panel.add(backBtn, BorderLayout.SOUTH);

    mainPanel.add(panel, "Leaderboard");
    cardLayout.show(mainPanel, "Leaderboard");
}

// ================= QUIZ SCREEN =================
void showQuizScreen(int quizId) {

    JLabel timerLabel = new JLabel("Time Left: 60");
    timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

    JPanel quizPanel = new JPanel(new BorderLayout());

    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
    content.add(Box.createRigidArea(new Dimension(0, 10)));

    java.util.ArrayList<String[]> questions = new java.util.ArrayList<>();
    java.util.ArrayList<Integer> correctAnswers = new java.util.ArrayList<>();
    java.util.ArrayList<ButtonGroup> groups = new java.util.ArrayList<>();

    try (Connection con = DBConnection.getConnection()) {

        String sql = "SELECT * FROM questions WHERE quiz_id=" + quizId;
        ResultSet rs = con.createStatement().executeQuery(sql);

        while (rs.next()) {
            questions.add(new String[]{
                    rs.getString("question_text"),
                    rs.getString("option1"),
                    rs.getString("option2"),
                    rs.getString("option3"),
                    rs.getString("option4")
            });

            correctAnswers.add(rs.getInt("correct_option"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    // ❓ SHOW QUESTIONS
    for (int i = 0; i < questions.size(); i++) {

        String[] q = questions.get(i);

        content.add(new JLabel("Q" + (i + 1) + ": " + q[0]));

        JRadioButton r1 = new JRadioButton(q[1]);
        JRadioButton r2 = new JRadioButton(q[2]);
        JRadioButton r3 = new JRadioButton(q[3]);
        JRadioButton r4 = new JRadioButton(q[4]);

        ButtonGroup bg = new ButtonGroup();
        bg.add(r1); bg.add(r2); bg.add(r3); bg.add(r4);

        groups.add(bg);

        content.add(r1);
        content.add(r2);
        content.add(r3);
        content.add(r4);
    }
    quizPanel.add(timerLabel, BorderLayout.NORTH);

    JScrollPane scroll = new JScrollPane(content);

    

    JButton submit = new JButton("Submit Quiz");
    int[] timeLeft = {60}; // 60 seconds

    Timer timer = new Timer(1000, null);

    timer.addActionListener(e -> {
        timeLeft[0]--;

        timerLabel.setText("Time Left: " + timeLeft[0] + "s");

        if (timeLeft[0] <= 0) {
            timer.stop();

            JOptionPane.showMessageDialog(null, "⏰ Time's up!");

            submit.doClick(); 
        }
    });

    timer.start();
    JButton backBtn = new JButton("⬅ Back");

    JPanel bottom = new JPanel();
    bottom.add(submit);
    bottom.add(backBtn);

    quizPanel.add(scroll, BorderLayout.CENTER);
    quizPanel.add(bottom, BorderLayout.SOUTH);

    mainPanel.add(quizPanel, "QuizScreen");
    cardLayout.show(mainPanel, "QuizScreen");

    // 🔙 BACK BUTTON
    backBtn.addActionListener(e -> cardLayout.show(mainPanel, STUDENT_SCREEN));


    // 🎯 SUBMIT
    submit.addActionListener(e -> {
        timer.stop();

        int score = 0;

        for (int i = 0; i < groups.size(); i++) {

            int selected = -1;
            int j = 1;

            java.util.Enumeration<AbstractButton> buttons = groups.get(i).getElements();

            while (buttons.hasMoreElements()) {
                AbstractButton btn = buttons.nextElement();
                if (btn.isSelected()) selected = j;
                j++;
            }

            if (selected == correctAnswers.get(i)) {
                score++;
            }
        }

        JOptionPane.showMessageDialog(null,
            "Quiz Completed!\nYour Score: " + score,
            "Result",
            JOptionPane.INFORMATION_MESSAGE);

        saveResult(quizId, score);

        cardLayout.show(mainPanel, STUDENT_SCREEN); // auto back after submit
    });
}


// ================= SAVE RESULT =================
void saveResult(int quizId, int score) {
    try (Connection con = DBConnection.getConnection()) {

        PreparedStatement pst = con.prepareStatement(
                "INSERT INTO results(user_id, quiz_id, score, date_taken) VALUES(?,?,?,NOW())"
        );

        pst.setInt(1, UserSession.currentUser.id);
        pst.setInt(2, quizId);
        pst.setInt(3, score);

        pst.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}


// ================= STUDENT PANEL =================
class StudentPanel extends JPanel {

    JTable table;

    StudentPanel() {

        setLayout(new BorderLayout());

        JLabel header = new JLabel("📚 Available Quizzes", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        table = new JTable();

        // ✅ Selection UI improve
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(Color.CYAN);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.setGridColor(new Color(220, 220, 220));

        loadQuizzes();

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        card.add(new JScrollPane(table));

        add(card, BorderLayout.CENTER);

        JLabel selectedLabel = new JLabel("No quiz selected");

        JButton startBtn = new JButton(" Start Quiz");
        JButton logout = new JButton(" Logout");
        JButton coursesBtn = new JButton("Courses");
        JButton myCoursesBtn = new JButton("My Courses");

        coursesBtn.addActionListener(e -> {
            mainPanel.add(new CoursePanel(), "Courses"); // refresh
            cardLayout.show(mainPanel, "Courses");
        });

        myCoursesBtn.addActionListener(e -> {
            mainPanel.add(new MyCoursesPanel(), "MyCourses"); // refresh
            cardLayout.show(mainPanel, "MyCourses");
        });


        // 🎯 Row selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String title = table.getValueAt(row, 1).toString();
                selectedLabel.setText("Selected: " + title);
            }
        });

        // 🎯 START BUTTON
        startBtn.addActionListener(e -> {

    int row = table.getSelectedRow();

    if (row == -1) {
        JOptionPane.showMessageDialog(this, "⚠️ Please select a quiz first!");
        return;
    }

    int quizId = (int) table.getValueAt(row, 0);

    try (Connection con = DBConnection.getConnection()) {

        // 🔒 CHECK IF ALREADY ATTEMPTED
        PreparedStatement checkAttempt = con.prepareStatement(
            "SELECT * FROM results WHERE user_id=? AND quiz_id=?"
        );

        checkAttempt.setInt(1, UserSession.currentUser.id);
        checkAttempt.setInt(2, quizId);

        ResultSet rsAttempt = checkAttempt.executeQuery();

        if (rsAttempt.next()) {
            JOptionPane.showMessageDialog(this, "❌ You have already attempted this quiz!");
            return;
        }

        // 🔒 CHECK ENROLLMENT (your existing logic)
        PreparedStatement checkEnroll = con.prepareStatement(
            "SELECT * FROM quizzes q " +
            "JOIN enrollments e ON q.course_id = e.course_id " +
            "WHERE q.quiz_id=? AND e.student_id=?"
        );

        checkEnroll.setInt(1, quizId);
        checkEnroll.setInt(2, UserSession.currentUser.id);

        ResultSet rsEnroll = checkEnroll.executeQuery();

        if (!rsEnroll.next()) {
            JOptionPane.showMessageDialog(this, "🔒 You are not enrolled in this course!");
            return;
        }

        // ✅ ALLOW QUIZ
        showQuizScreen(quizId);

    } catch (Exception ex) {
        ex.printStackTrace();
    }
});



        // 🔒 LOGOUT
        logout.addActionListener(e -> cardLayout.show(mainPanel, LOGIN_SCREEN));

        JButton leaderboardBtn = new JButton("Leaderboard");

        JPanel bottom = new JPanel();
        bottom.add(selectedLabel);
        bottom.add(startBtn);
        bottom.add(leaderboardBtn);
        bottom.add(logout);
        bottom.add(coursesBtn);
        bottom.add(myCoursesBtn);


        add(bottom, BorderLayout.SOUTH);

        leaderboardBtn.addActionListener(e -> {
            showLeaderboard();
        });
    }

    void loadQuizzes() {
        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT quiz_id, title, subject FROM quizzes";
            ResultSet rs = con.createStatement().executeQuery(sql);

            java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("quiz_id"),
                        rs.getString("title"),
                        rs.getString("subject")
                });
            }

            String[] cols = {"ID", "Title", "Subject"};

            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class CoursePanel extends JPanel {

    JTable table;

    CoursePanel() {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("📚 Available Courses", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        table = new JTable();

        loadCourses();

        JButton enrollBtn = new JButton("Enroll");
        JButton backBtn = new JButton("Back");

        JPanel bottom = new JPanel();
        bottom.add(enrollBtn);
        bottom.add(backBtn);

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // 🎯 ENROLL BUTTON
        enrollBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course!");
                return;
            }

            int courseId = (int) table.getValueAt(row, 0);

            try (Connection con = DBConnection.getConnection()) {

                // ❌ check duplicate
                PreparedStatement check = con.prepareStatement(
                        "SELECT * FROM enrollments WHERE student_id=? AND course_id=?"
                );
                check.setInt(1, UserSession.currentUser.id);
                check.setInt(2, courseId);

                ResultSet rs = check.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "Already enrolled!");
                    return;
                }

                // ✅ insert
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO enrollments(student_id, course_id) VALUES(?,?)"
                );
                ps.setInt(1, UserSession.currentUser.id);
                ps.setInt(2, courseId);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this, "Enrolled Successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        backBtn.addActionListener(e ->
                cardLayout.show(mainPanel, STUDENT_SCREEN));
    }

    void loadCourses() {
        try (Connection con = DBConnection.getConnection()) {

            ResultSet rs = con.createStatement().executeQuery("SELECT * FROM courses");

            java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("description")
                });
            }

            String[] cols = {"ID", "Course", "Description"};

            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class MyCoursesPanel extends JPanel {

    JTable table;

    MyCoursesPanel() {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("🎓 My Courses", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        table = new JTable();

        loadMyCourses();

        JButton backBtn = new JButton("Back");

        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(backBtn, BorderLayout.SOUTH);

        backBtn.addActionListener(e ->
                cardLayout.show(mainPanel, STUDENT_SCREEN));
    }

    void loadMyCourses() {
    try (Connection con = DBConnection.getConnection()) {

        String sql =
            "SELECT c.course_name, " +
            "COUNT(DISTINCT q.quiz_id) AS total_quiz, " +
            "COUNT(DISTINCT r.quiz_id) AS completed " +
            "FROM courses c " +
            "JOIN enrollments e ON c.course_id = e.course_id " +
            "LEFT JOIN quizzes q ON c.course_id = q.course_id " +
            "LEFT JOIN results r ON q.quiz_id = r.quiz_id AND r.user_id=? " +
            "WHERE e.student_id=? " +
            "GROUP BY c.course_id";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, UserSession.currentUser.id);
        ps.setInt(2, UserSession.currentUser.id);

        ResultSet rs = ps.executeQuery();

        java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();

        while (rs.next()) {

            int total = rs.getInt("total_quiz");
            int done = rs.getInt("completed");

            int progress = (total == 0) ? 0 : (done * 100 / total);

            list.add(new Object[]{
                    rs.getString("course_name"),
                    progress + "%"
            });
        }

        String[] cols = {"Course", "Progress"};

        Object[][] data = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }

        table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}


            //MANAGE QUIZ PANEL
JPanel manageQuizPanel() {

    JPanel panel = new JPanel(new BorderLayout());

    String[] cols = {"Quiz ID", "Title", "Admin Name"};
    JTable table = new JTable();

    JButton loadBtn = new JButton("Load Quizzes");
    JButton backBtn = new JButton("Back");

    JPanel top = new JPanel();
    top.add(loadBtn);
    top.add(backBtn);

    panel.add(top, BorderLayout.NORTH);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);

    // 🔥 LOAD DATA (FIXED)
    loadBtn.addActionListener(e -> {
        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT q.quiz_id, q.title, u.username " +
                         "FROM quizzes q JOIN users u ON q.created_by = u.user_id";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // ✅ Use ArrayList (no rs.last() issue)
            java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();

            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("quiz_id"),
                    rs.getString("title"),
                    rs.getString("username")
                });
            }

            // 🔍 Debug (optional)
            System.out.println("Rows fetched: " + list.size());

            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, ex.getMessage());
        }
    });

    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Admin"));

    return panel;
}


JPanel addCoursePanel() {

    JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

    JTextField courseNameField = new JTextField();
    JTextField descField = new JTextField();

    JButton addBtn = new JButton("Add Course");
    JButton backBtn = new JButton("Back");

    panel.add(new JLabel("Course Name:"));
    panel.add(courseNameField);

    panel.add(new JLabel("Description:"));
    panel.add(descField);

    panel.add(addBtn);
    panel.add(backBtn);

    addBtn.addActionListener(e -> {
        try (Connection con = DBConnection.getConnection()) {

            if (courseNameField.getText().isEmpty() || descField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO courses(course_name, description) VALUES(?,?)"
            );

            pst.setString(1, courseNameField.getText());
            pst.setString(2, descField.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Course Added Successfully! 🎉");

            courseNameField.setText("");
            descField.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    });

    backBtn.addActionListener(e ->
        cardLayout.show(mainPanel, ADMIN_SCREEN)
    );

    return panel;
}

            //CREATE QUIZ PANEL



JPanel createQuizPanel() {

    JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
    JComboBox<String> courseDropdown = new JComboBox<>();

    courseDropdown.addItem("Select Course"); // ✅ first
    loadCourseDropdown(courseDropdown);

    JTextField titleField = new JTextField();
    JTextField subjectField = new JTextField();

    JButton createBtn = new JButton("Create Quiz");
    JButton backBtn = new JButton("Back");

    panel.add(new JLabel("Quiz Title:"));
    panel.add(titleField);

    panel.add(new JLabel("Subject:"));
    panel.add(subjectField);

    panel.add(new JLabel("Select Course:"));   // ✅ ADD THIS
    panel.add(courseDropdown);

    panel.add(createBtn);
    panel.add(backBtn);



    createBtn.addActionListener(e -> {
        try (Connection con = DBConnection.getConnection()) {
            String selected = (String) courseDropdown.getSelectedItem();

            if (selected == null || selected.equals("Select Course")) {
                JOptionPane.showMessageDialog(this, "Please select a course!");
                return;
            }

            int courseId = Integer.parseInt(selected.split(" - ")[0]);

            if (titleField.getText().isEmpty() || subjectField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Fill all fields!");
                return;
            }

            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO quizzes(title, subject, course_id, created_by) VALUES(?,?,?,?)"
            );

            pst.setString(1, titleField.getText());
            pst.setString(2, subjectField.getText());
            pst.setInt(3, courseId);
            pst.setInt(4, UserSession.currentUser.id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Quiz Created Successfully! 🎉");

            titleField.setText("");
            subjectField.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    });

    backBtn.addActionListener(e ->
        cardLayout.show(mainPanel, ADMIN_SCREEN)
    );

    return panel;
}

void loadCourseDropdown(JComboBox<String> dropdown) {
    try (Connection con = DBConnection.getConnection()) {

        String sql = "SELECT course_id, course_name FROM courses";
        ResultSet rs = con.createStatement().executeQuery(sql);

        while (rs.next()) {
            dropdown.addItem(
                rs.getInt("course_id") + " - " + rs.getString("course_name")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


        // ❓ MANAGE QUESTIONS PANEL
JPanel manageQuestionPanel() {

    JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));

    JComboBox<String> quizDropdown = new JComboBox<>();

// ✅ CALL METHOD HERE
    loadQuizDropdown(quizDropdown);

    JTextField q = new JTextField();
    JTextField o1 = new JTextField();
    JTextField o2 = new JTextField();
    JTextField o3 = new JTextField();
    JTextField o4 = new JTextField();
    JTextField ans = new JTextField();

    JButton addBtn = new JButton("Add Question");
    JButton backBtn = new JButton("Back");

    // 🔥 LOAD QUIZZES FROM DB


    // 🧾 UI
    panel.add(new JLabel("Select Quiz:"));
    panel.add(quizDropdown);

    panel.add(new JLabel("Question:"));
    panel.add(q);

    panel.add(new JLabel("Option 1:"));
    panel.add(o1);

    panel.add(new JLabel("Option 2:"));
    panel.add(o2);

    panel.add(new JLabel("Option 3:"));
    panel.add(o3);

    panel.add(new JLabel("Option 4:"));
    panel.add(o4);

    panel.add(new JLabel("Correct (1-4):"));
    panel.add(ans);

    panel.add(addBtn);
    panel.add(backBtn);

    // 🚀 ADD BUTTON LOGIC
    addBtn.addActionListener(e -> {
        try (Connection con = DBConnection.getConnection()) {

            // ✅ Validation
            if (q.getText().isEmpty() || o1.getText().isEmpty() ||
                o2.getText().isEmpty() || o3.getText().isEmpty() ||
                o4.getText().isEmpty() || ans.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            // 🎯 GET SELECTED QUIZ ID
            String selected = (String) quizDropdown.getSelectedItem();
            int quizId = Integer.parseInt(selected.split(" - ")[0]);

            PreparedStatement pst = con.prepareStatement(
                "INSERT INTO questions(quiz_id, question_text, option1, option2, option3, option4, correct_option) VALUES(?,?,?,?,?,?,?)"
            );

            pst.setInt(1, quizId);
            pst.setString(2, q.getText());
            pst.setString(3, o1.getText());
            pst.setString(4, o2.getText());
            pst.setString(5, o3.getText());
            pst.setString(6, o4.getText());
            pst.setInt(7, Integer.parseInt(ans.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question Added Successfully! 🎉");

            // 🧹 CLEAR FIELDS
            q.setText("");
            o1.setText("");
            o2.setText("");
            o3.setText("");
            o4.setText("");
            ans.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    backBtn.addActionListener(e -> cardLayout.show(mainPanel, "Admin"));

    return panel;
}

void loadQuizDropdown(JComboBox<String> quizDropdown) {
    try (Connection con = DBConnection.getConnection()) {

        String sql = "SELECT quiz_id, title FROM quizzes";
        ResultSet rs = con.createStatement().executeQuery(sql);

        quizDropdown.removeAllItems(); // clear first

        while (rs.next()) {
            quizDropdown.addItem(
                rs.getInt("quiz_id") + " - " + rs.getString("title")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // 📊 VIEW RESULTS PANEL
JPanel viewResultPanel() {

    JPanel panel = new JPanel(new BorderLayout());

    String[] cols = {"Student", "Quiz", "Score", "Date"};
    JTable table = new JTable();

    JButton loadBtn = new JButton("Load Results");
    JButton backBtn = new JButton("⬅ Back");

    // 🌸 STYLE TABLE
    table.setRowHeight(30);
    table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

    JPanel top = new JPanel();
    top.add(loadBtn);
    top.add(backBtn);

    panel.add(top, BorderLayout.NORTH);
    panel.add(new JScrollPane(table), BorderLayout.CENTER);

    // 🔥 LOAD RESULTS
    loadBtn.addActionListener(e -> {
        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT u.username, q.title, r.score, r.date_taken " +
                         "FROM results r " +
                         "JOIN users u ON r.user_id = u.user_id " +
                         "JOIN quizzes q ON r.quiz_id = q.quiz_id";

            ResultSet rs = con.createStatement().executeQuery(sql);

            java.util.ArrayList<Object[]> list = new java.util.ArrayList<>();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("username"),
                        rs.getString("title"),
                        rs.getInt("score"),
                        rs.getString("date_taken")
                });
            }

            Object[][] data = new Object[list.size()][4];
            for (int i = 0; i < list.size(); i++) {
                data[i] = list.get(i);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, cols));

            if (list.size() == 0) {
                JOptionPane.showMessageDialog(panel, "No results found!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(panel, ex.getMessage());
        }
    });

    backBtn.addActionListener(e ->
        cardLayout.show(mainPanel, ADMIN_SCREEN)
    );

    return panel;
}

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizManagementSystem().setVisible(true));
    }
}