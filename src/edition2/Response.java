package edition2;

import javax.servlet.*;
import java.io.*;
import java.util.Locale;

public class Response implements ServletResponse {
    private static final int BUFFER_SIZE = 1024;
    Request request;
    OutputStream output;
    PrintWriter writer;
    StringBuilder stringBuilder = new StringBuilder();

    public Response(OutputStream output) {
        this.output = output;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void write(byte[] b,int start,int end) throws IOException {
        for(int i = start;i<end;i++) {
            this.stringBuilder.append((char) b[i]);
        }
    }
    public void sendStaticResource() throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        try {
            File file = new File(Constants.WEB_ROOT, request.getUri());
            fis = new FileInputStream(file);
            /*
             HTTP Response = Status-Line
               *(( general-header | response-header | entity-header ) CRLF)
               CRLF
               [ message-body ]
             Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
            */
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1) {
//                    write(bytes,0,ch);
                output.write(bytes, 0, ch);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
        }
        catch (Exception e) {
            String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: 23\r\n" +
                    "\r\n" +
                    "<h1>File Not Found</h1>";
            output.write(errorMessage.getBytes());
            // thrown if cannot instantiate a File object
            System.out.println(e.toString() );
        }
        finally {
            if (fis!=null)
                fis.close();
        }
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        writer = new PrintWriter(output, true);
        return writer;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentLengthLong(long l) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
