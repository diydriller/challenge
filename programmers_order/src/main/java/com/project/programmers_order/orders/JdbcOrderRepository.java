package com.project.programmers_order.orders;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import static java.util.Optional.ofNullable;

import static com.project.programmers_order.utils.DateTimeUtils.dateTimeOf;


@Repository
@RequiredArgsConstructor
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Orders> findOrderById(Long id) {

        List<Orders> results = jdbcTemplate.query(
                "SELECT * FROM orders WHERE seq=?",
                mapper1,
                id
        );

        return ofNullable(results.isEmpty() ? null : results.get(0));
    }



    @Override
    public Review createReview(Long userId, Long productId, String content) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps=connection.prepareStatement("INSERT INTO reviews(user_seq,product_seq,content) VALUES(?,?,?)",new String[]{"seq"});
            ps.setLong(1,userId);
            ps.setLong(2,productId);
            ps.setString(3,content);
            return ps;
        },keyHolder);
        Long insertId=keyHolder.getKey().longValue();

        List<Review> result=jdbcTemplate.query(
                "SELECT * FROM reviews WHERE seq=?",
                mapper2,
                insertId
        );
        return result.get(0);
    }

    @Override
    public void updateOrderByReviewSeq(Long id, Long reviewSeq) {
        jdbcTemplate.update("UPDATE orders SET review_seq=? WHERE seq=?",reviewSeq,id);
    }

    @Override
    public List<Orders> findAllOrder(Long userId, Long offset, int size) {
        List<Orders> results=jdbcTemplate.query(
                "SELECT * FROM orders WHERE user_seq=? ORDER BY seq DESC LIMIT ?,?  ",
                mapper1,
                userId,offset,size
        );
        return results;
    }

    @Override
    public Review findReviewById(Long id) {
        List<Review> results = jdbcTemplate.query(
                "SELECT * FROM reviews WHERE seq=?",
                mapper2,
                id
        );
        return results.isEmpty()?null:results.get(0);
    }

    @Override
    public void updateOrderByState(Long id, State state) {
        jdbcTemplate.update("UPDATE orders SET state=? WHERE seq=?",state.value(),id);
    }

    @Override
    public void updateOrderByStateAndMsg(Long id, State state, String message) {
        jdbcTemplate.update("UPDATE orders SET state=?,reject_msg=?  WHERE seq=?",state.value(),message,id);
    }

    @Override
    public void updateOrderByStateAndCompleteTime(Long id, State state, Timestamp time) {
        jdbcTemplate.update("UPDATE orders SET state=?,completed_at=?  WHERE seq=?",state.value(),time,id);
    }

    @Override
    public void updateOrderByStateAndRejectTime(Long id, State state, Timestamp time) {
        jdbcTemplate.update("UPDATE orders SET state=?,rejected_at=?  WHERE seq=?",state.value(),time,id);
    }


    static RowMapper<Orders> mapper1 = (rs, rowNum) ->
            new Orders.OrdersBuilder()
                    .seq(rs.getLong("seq"))
                    .userSeq(rs.getLong("user_seq"))
                    .productSeq(rs.getLong("product_seq"))
                    .reviewSeq(rs.getLong("review_seq"))
                    .requestMsg(rs.getString("request_msg"))
                    .rejectMsg(rs.getString("reject_msg"))
                    .state(State.of(rs.getString("state")))
                    .completedAt(dateTimeOf(rs.getTimestamp("completed_at")))
                    .rejectedAt(dateTimeOf(rs.getTimestamp("rejected_at")))
                    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                    .build();

    static RowMapper<Review> mapper2 = (rs, rowNum) ->
            new Review.ReviewBuilder()
                    .seq(rs.getLong("seq"))
                    .productId(rs.getLong("product_seq"))
                    .content(rs.getString("content"))
                    .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                    .build();


}
