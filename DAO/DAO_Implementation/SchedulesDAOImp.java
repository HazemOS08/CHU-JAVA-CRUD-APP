package DAO.DAO_Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import DAO.SchedulesDAO;
import Table_Classes.Schedules;
import db.DbHelper;

public class SchedulesDAOImp implements SchedulesDAO {

    private Schedules setToSch(ResultSet r) throws SQLException {

    Schedules schedule = new Schedules();
    schedule.setId(r.getInt("id"));
    schedule.setUserId(r.getInt("user_id"));
    schedule.setPosId(r.getInt("pos_id"));
    schedule.setStartTime(LocalDateTime.parse(r.getString("start_time")));
    schedule.setEndTime(LocalDateTime.parse(r.getString("end_time")));

    
    schedule.setPositionName(r.getString("position_name"));

    return schedule;

    };

    @Override
    public Schedules findById(int id) {

        String s = "SELECT s.*, p.name AS position_name " +
                   "FROM Schedules s " +
                   "JOIN Positions p ON s.pos_id = p.id " +
                   "WHERE s.id = ?";

        try (Connection con = DbHelper.getConnection();
             PreparedStatement st = con.prepareStatement(s)) {

            st.setInt(1, id);

            try (ResultSet r = st.executeQuery()) {

                if (r.next()) {

                    return setToSch(r);

                }
            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return null;

    }

    @Override
    public List<Schedules> findByUserIdAndRange(LocalDateTime start, LocalDateTime end, int userId) {

        String s = "SELECT s.*, p.name AS position_name " +
                   "FROM Schedules s " +
                   "JOIN Positions p ON s.pos_id = p.id " +
                   "WHERE s.user_id = ? "+
                   "AND s.start_time>=? " +
                   "AND s.start_time<=? "+
                   "ORDER BY s.start_time ASC";

        List<Schedules> l = new ArrayList<>();

        try (Connection con = DbHelper.getConnection();
             PreparedStatement st = con.prepareStatement(s)) {

            st.setInt(1, userId);
            st.setString(2, start.toString());
            st.setString(3, end.toString());

            try (ResultSet r = st.executeQuery()) {

                while (r.next()) {

                    l.add(setToSch(r));

                }

            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return l;

    }

    @Override
    public List<Schedules> findByDepartmentIdAndRange(LocalDateTime start, LocalDateTime end, int deptId) {

        String s = "SELECT s.*, p.name AS position_name " +
                   "FROM Schedules s " +
                   "JOIN Positions p ON s.pos_id = p.id " +
                   "WHERE p.dept_id = ? " +
                   "AND s.start_time>= ? " +
                   "AND s.start_time<= ? "+
                   "ORDER BY s.start_time ASC, user_id ASC";

        List<Schedules> l = new ArrayList<>();

        try (Connection con = DbHelper.getConnection();
             PreparedStatement st = con.prepareStatement(s)) {

            st.setInt(1, deptId);
            st.setString(2, start.toString());
            st.setString(3, end.toString());

            try (ResultSet r = st.executeQuery()) {

                while (r.next()) {

                    l.add(setToSch(r));

                }

            }

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return l;

    }

    @Override
    public boolean insert(Schedules schedule) {

        String s = "INSERT INTO Schedules (user_id,start_time,end_time,pos_id) " +
                   "VALUES(?,?,?,?)";
        String S = "SELECT last_insert_rowid()"; // get last assigned id

        try (Connection con = DbHelper.getConnection();
             PreparedStatement st = con.prepareStatement(s)) {

            st.setInt(1, schedule.getUserId());
            st.setString(2, schedule.getStartTime().toString());
            st.setString(3, schedule.getEndTime().toString());
            st.setInt(4,schedule.getPosId());

            int rowInserted = st.executeUpdate();

            if (rowInserted > 0) {

                try (Statement stm = con.createStatement()) {

                    ResultSet r = stm.executeQuery(S);

                    if (r.next()) {

                        schedule.setId(r.getInt(1));
                    }
                }
            }

            return true;

        } catch (SQLException e) {

            System.out.println("Error : " + e.getMessage());

        }

        return false;

    }

    @Override
    public boolean update(Schedules schedule) {

        String s = "UPDATE Schedules SET user_id=?,start_time=?,end_time=?,pos_id=?" +
                   "WHERE id=?";

        try (Connection con = DbHelper.getConnection();

             PreparedStatement st = con.prepareStatement(s)) {

            st.setInt(1, schedule.getUserId());
            st.setString(2, schedule.getStartTime().toString());
            st.setString(3, schedule.getEndTime().toString());
            st.setInt(4, schedule.getPosId());
            st.setInt(5, schedule.getId());

            return st.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return false;

    }

    @Override
    public boolean delete(int id) {

        String s = "DELETE FROM Schedules WHERE id= ?";

        try (Connection con = DbHelper.getConnection();
             PreparedStatement st = con.prepareStatement(s)) {

            st.setInt(1, id);

            int r = st.executeUpdate(); // n of deleted rows

            return r > 0;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return false;

    }
}
