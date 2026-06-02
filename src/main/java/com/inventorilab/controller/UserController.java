package com.inventorilab.controller;

import com.inventorilab.dto.response.UserResponse;
import com.inventorilab.response.PagingResponse;
import com.inventorilab.response.WebResponse;
import com.inventorilab.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<UserResponse>> getById(@PathVariable Long id) {
        UserResponse responseData = userService.getById(id);
        WebResponse<UserResponse> response = WebResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("User ditemukan!")
                .data(responseData)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<WebResponse<List<UserResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.DESC.name()) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserResponse> pageData = userService.getAll(pageable);

        PagingResponse paging = PagingResponse.builder()
                .currentPage(pageData.getNumber())
                .totalPages(pageData.getTotalPages())
                .size(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .build();

        WebResponse<List<UserResponse>> response = WebResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("Berhasil mendapatkan daftar user!")
                .data(pageData.getContent())
                .paging(paging)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable Long id) {
        userService.delete(id);
        WebResponse<String> response = WebResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .message("User dengan ID " + id + " berhasil dihapus!")
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}
