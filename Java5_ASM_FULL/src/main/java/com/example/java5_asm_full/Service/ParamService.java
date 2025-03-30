package com.example.java5_asm_full.Service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ParamService {
    @Autowired
    HttpServletRequest request;

    /**
     * Đọc chuỗi giá trị của tham số
     * @param name tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public String getString(String name, String defaultValue){
        String value = request.getParameter(name);
        return (value != null) ? value : defaultValue;
    }

    /**
     * Đọc số nguyên giá trị của tham số
     * @param name tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public int getInt(String name, int defaultValue){
        String value = request.getParameter(name);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    /**
     * Đọc số thực giá trị của tham số
     * @param name tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public double getDouble(String name, double defaultValue){
        String value = request.getParameter(name);
        return (value != null) ? Double.parseDouble(value) : defaultValue;
    }


    /**
     * Đọc giá trị boolean của tham số
     * @param name tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public boolean getBoolean(String name, boolean defaultValue){
        String value = request.getParameter(name);
        return (value != null) ? true : defaultValue;
    }


    /**
     * Đọc giá trị thời gian của tham số
     * @param name tên tham số
     * @param pattern là định dạng thời gian
     * @return giá trị tham số hoặc null nếu không tồn tại
     * @throws RuntimeException lỗi sai định dạng
     */
    public Date getDate(String name, String pattern){
        String value = request.getParameter(name);

        if (value != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                return dateFormat.parse(value); // Phân tích giá trị thành đối tượng Date
            } catch (ParseException e) {
                throw new RuntimeException("Lỗi sai định dạng thời gian: " + value, e); // Ném ngoại lệ nếu định dạng không hợp lệ
            }
        }
        return null;

    }


    /**
     * Lưu file upload vào thư mục
     * @param file chứa file upload từ client
     * @param path đường dẫn tính từ webroot
     * @return đối tượng chứa file đã lưu hoặc null nếu không có file upload
     * @throws RuntimeException lỗi lưu file
     */
    public File save(MultipartFile file, String path) {
        if (file == null || file.isEmpty()) {
            return null; // Trả về null nếu không có file upload
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs(); // Tạo thư mục nếu nó không tồn tại
        }
        // Tạo đường dẫn đầy đủ cho file sẽ lưu
        File savedFile = new File(dir, file.getOriginalFilename());

        try {
            // Lưu file vào đường dẫn đã chỉ định
            file.transferTo(savedFile);
            return savedFile; // Trả về đối tượng File đã lưu
        } catch (IOException e) {
            throw new RuntimeException("Lỗi lưu file: " + e.getMessage(), e); // Ném ngoại lệ nếu có lỗi xảy ra
        }
    }


}
