package com.lph.selfcareapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.lph.selfcareapp.dto.MedicalReport;
import com.lph.selfcareapp.serviceAPI.RetrofitInstance;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfDisplayActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener, OnPageErrorListener {
    private PDFView pdfView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pdf_viewer);
        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);
        int appoid = getIntent().getIntExtra("appoid", 0);
        callGenerateMedicalReportApi(appoid);

    }

    private void callGenerateMedicalReportApi(int appoid) {
        progressBar.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.GONE);

        String jwt = getSharedPreferences("UserData", MODE_PRIVATE).getString("jwt", "");
        RetrofitInstance.getService(jwt).diagnose(appoid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);

                // This 'response' object is Retrofit's equivalent of ResponseEntity
                if (response.isSuccessful()) {
                    int statusCode = response.code(); // e.g., 200 OK
                    okhttp3.Headers headers = response.headers(); // Access response headers

                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        try {
                            InputStream inputStream = responseBody.byteStream();
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = inputStream.read(buffer)) != -1) {
                                byteArrayOutputStream.write(buffer, 0, bytesRead);
                            }
                            byte[] pdfBytes = byteArrayOutputStream.toByteArray();
//                            File pdfFile = savePdfBytesToFile(pdfBytes);
                            displayPdfFromBytes(pdfBytes);

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(PdfDisplayActivity.this, "Error processing PDF data.", Toast.LENGTH_LONG).show();
                        } finally {
                            try {
                                responseBody.close(); // Close the ResponseBody stream
                            } catch (Exception e) { /* ignore */ }
                        }
                    } else {
                        Log.d("PDFViewer", "response is empty");
                    }

                } else {
                    // API call was not successful (e.g., 400 Bad Request, 500 Internal Server Error)
                    int errorCode = response.code();
                    ResponseBody errorBody = response.errorBody(); // Get error body if available

                    String errorMessage = "API Error: " + errorCode;
                    if (errorBody != null) {
                        try {
                            errorMessage += "\n" + errorBody.string(); // Convert error body to string
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                errorBody.close();
                            } catch (Exception e) { /* ignore */ }
                        }
                    }
                    Toast.makeText(PdfDisplayActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                // Network error (no internet, host unreachable, timeout, etc.)
                Log.d("PDFViewer", "PDF error with " + t.getMessage() + " pages.");
                Toast.makeText(PdfDisplayActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void displayPdfFromFile(File pdfFile) {
        Log.d("PDFViewer", "Attempting to load PDF from file: " + pdfFile.getAbsolutePath());
        pdfView.fromFile(pdfFile) // Thay đổi từ fromBytes() sang fromFile()
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0) // Luôn đặt trang mặc định
                .onLoad(this)
                .onPageChange(this)
                .onPageError(this)
                .load();
        Log.d("PDFViewer", "pdfView.load() from file called.");
    }

    private void displayPdfFromBytes(byte[] pdfBytes) {
        Log.d("PDFViewer", "First bytes: " + Arrays.toString(Arrays.copyOf(pdfBytes, 10)));
        Log.d("PDFViewer", "Calling pdfView.fromBytes().load() with " + pdfBytes.length + " bytes.");
        pdfView.setVisibility(View.VISIBLE);
        pdfView.fromBytes(pdfBytes)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .onLoad(this)
                .onPageChange(this)
                .onPageError(this)
                .load();
        Log.d("PDFViewer", "pdfView.load() called.");
    }

    private File savePdfBytesToFile(byte[] pdfBytes) {
        // Lấy thư mục cache nội bộ của ứng dụng.
        // Đây là nơi an toàn để lưu các file tạm thời, chỉ ứng dụng của bạn mới có thể truy cập.
        File cacheDir = getCacheDir();

        // Tạo một đối tượng File cho file PDF tạm thời
        // Tên file là "debug_medical_report.pdf"
        File tempPdfFile = new File(cacheDir, "debug_medical_report.pdf");

        try (FileOutputStream fos = new FileOutputStream(tempPdfFile)) {
            // Ghi toàn bộ mảng byte vào file
            fos.write(pdfBytes);
            // Đảm bảo tất cả dữ liệu được ghi xuống đĩa
            fos.flush();

            // Log thông báo thành công và đường dẫn của file
            Log.d("PDF_DEBUG", "PDF bytes saved to: " + tempPdfFile.getAbsolutePath());
            // Hiển thị thông báo Toast cho người dùng
            Toast.makeText(this, "PDF saved for debug: " + tempPdfFile.getName(), Toast.LENGTH_LONG).show();

            return tempPdfFile; // Trả về đối tượng File
        } catch (IOException e) {
            // Log và hiển thị lỗi nếu có vấn đề khi ghi file
            Log.e("PDF_DEBUG", "Failed to save PDF bytes to file", e);
            Toast.makeText(this, "Failed to save PDF for debug.", Toast.LENGTH_LONG).show();
            return null; // Trả về null nếu có lỗi
        }
    }

    // PDFViewer Listeners
    @Override
    public void loadComplete(int nbPages) {

        Log.d("PDFViewer", "PDF loaded with " + nbPages + " pages.");
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Log.d("PDFViewer", "PDF change with " + page + " pages.");
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.d("PDFViewer", "PDF error with " + t.toString() + " pages.");
    }
}