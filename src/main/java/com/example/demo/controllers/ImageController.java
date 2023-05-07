package com.example.demo.controllers;

import com.example.demo.entity.Image;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bookstore?useSSL=false", "root", "sql.14159265");

            String sql = "SELECT cover FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if(!rs.next()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Image image = new Image(rs.getBytes("cover"));

            rs.close();
            stmt.close();
            conn.close();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(image.getData().length);
            return new ResponseEntity<byte[]>(image.getData(), headers, HttpStatus.OK);

        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
