package com.api.wechat;

import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WechatApplication {
    private static boolean stops;

    public static void main(String[] args) throws Exception {
           SpringApplication.run(WechatApplication.class, args);



/*        FromDate fromDate = new FromDate(new Date(),"from");
        ToDate toDate=new ToDate();
        BeanUtils.copyProperties(fromDate,toDate);
        toDate.setFromdate(fromDate.getFromdate().getTime()+"");
        System.out.println(toDate);*/
        //       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //       Date date=new Date(Long.parseLong(String.valueOf(new Date().getTime())));
        //       System.out.println(date);
        //   System.out.println(String.valueOf(new Date().getTime()));
   /*     Stack<Number> numberStack=new Stack<>();
        Iterable<Integer> integers=new HashSet<>();
        ((HashSet<Integer>) integers).add(13);
        numberStack.pushAll(integers);

        Collection<Object> objects=new HashSet<>();
        numberStack.popAll(objects);

        Set<Integer> integers1=new HashSet<>();
        Set<Double> doubles1= new HashSet<>();
        Set<Number> numbers=union(integers1,doubles1);
        List<Integer> list=new ArrayList<>();
        swap(list,1,2);*/
    }

    public static void swap(List<?> list, int i, int j) {
        // list.set(j,list.get(i));
        swapHelper(list, i, j);
    }

    private static <E> void swapHelper(List<E> list, int i, int j) {
        list.set(i, list.set(j, list.get(i)));
    }
/*
    public static <E> void swap(List<E> list,int i,int j){

    }*/

    public static <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2) {
        return null;
    }

    public static <T extends Comparable<? super T>> T max(List<T> list) {
        Iterator<T> i = list.iterator();
        T result = i.next();
        while (i.hasNext()) {
            T t = i.next();
            if (t.compareTo(result) > 0) {
                result = t;
            }
        }
        return result;
    }

    public static <T> T max1(List list) {

        return (T) list.get(0);
    }

}

class ToDate {
    private String fromdate;
    private String name;

    @Override
    public String toString() {
        return "ToDate{" +
                "fromdate='" + fromdate + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public ToDate(String fromdate, String name) {
        this.fromdate = fromdate;
        this.name = name;
    }

    public ToDate() {
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class FromDate {
    private Date fromdate;
    private String name;

    public FromDate(Date fromdate, String name) {
        this.fromdate = fromdate;
        this.name = name;
    }

    public FromDate() {
    }

    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Stack<E> {
    private Object[] elements;
    private int size = 0;
    private static final int DEFALUT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFALUT_INITIAL_CAPACITY];
    }

    public void push(E e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public void pushAll(Iterable<? extends E> src) {
        for (E e : src) {
            push(e);
        }
    }

    @SuppressWarnings("uncheked")
    public E pop() {
        if (size == 0) throw new RuntimeException();
        Object result = elements[--size];
        elements[size] = null;
        return (E) result;
    }

    public void popAll(Collection<? super E> dst) {
        while (size > 0) {
            dst.add(pop());
        }
    }

    private void ensureCapacity() {
        if (size >= DEFALUT_INITIAL_CAPACITY) {
            elements = Arrays.copyOf(elements, 2 * elements.length);
        }
    }
}
