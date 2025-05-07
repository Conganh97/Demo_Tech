package com.demo.proxy;

import com.demo.proxy.proxyhandle.ServiceInvocationHandler;
import com.demo.proxy.service.MyService;
import com.demo.proxy.service.serviceimpl.InterfaceImpl;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

public class ProxyMain {
    public static void main(String[] args) throws Exception {
        proxyImplement();
        proxyCover();
    }

    @SneakyThrows
    public static void proxyImplement() {
        Class<?> interfaceClass = Class.forName("com.demo.proxy.service.MyService");
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InterfaceImpl());

        runPerform((MyService) proxyInstance);

    }

    @SneakyThrows
    public static void proxyCover() {
        Set<Class<? extends MyService>> serviceClasses = findImplementingClasses(MyService.class);

        // Lấy phương thức performTask từ interface Service
        Method performTaskMethod = MyService.class.getMethod("performTask", String.class);

        // Duyệt qua các lớp implement Service
        for (Class<? extends MyService> serviceClass : serviceClasses) {
            // Tạo instance của lớp
            MyService serviceInstance = serviceClass.getDeclaredConstructor().newInstance();

            // Bọc serviceInstance vào proxy
            MyService proxy = (MyService) createProxy(serviceInstance);

            // Gọi phương thức performTask qua reflection
            performTaskMethod.invoke(proxy, "Task for " + serviceClass.getSimpleName());

        }
    }

    // Phương thức bọc dịch vụ vào proxy
    public static Object createProxy(MyService service) {
        return Proxy.newProxyInstance(
                MyService.class.getClassLoader(),
                new Class<?>[]{MyService.class},
                new ServiceInvocationHandler(service)
        );
    }

    public static Set<Class<? extends MyService>> findImplementingClasses(Class<MyService> interfaceClass) {
        Reflections reflections = new Reflections("com.demo.proxy.service"); // Thay com.example bằng package của bạn
        return reflections.getSubTypesOf(interfaceClass);
    }

    public static void runPerform(MyService myService) {
        myService.performTask("Cong Anh");
    }
}
