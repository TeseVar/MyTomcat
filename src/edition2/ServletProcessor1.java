package edition2;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

public class ServletProcessor1 {
    /**
     * 跟据url传过来的类名加载相应的类
     * @param request request
     * @param response response
     */
    public void process(Request request,Response response){
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;
        try {
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            // the forming of repository is taken from the createClassLoader method in
            // org.apache.catalina.startup.ClassLoaderFactory
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString() ;
            urls[0] = new URL(null,repository,streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class myClass = null;
        try {
            myClass = loader.loadClass(servletName);
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Servlet servlet = null;

        try {
            servlet = (Servlet) myClass.newInstance();
            //直接传递不安全,封装一层,避免调用request 和 response内部的方法
            servlet.service(new RequestFacade(request),new ResponseFacade(response));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
