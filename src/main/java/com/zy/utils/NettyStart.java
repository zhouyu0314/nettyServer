//package com.zy.utils;
//
//import com.zy.netty.server.NettyServer;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NettyStart implements ApplicationListener<ContextRefreshedEvent> {
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        System.out.println("启动webstocket.....");
//        NettyServer.getInstnal().start();
//    }
//}
