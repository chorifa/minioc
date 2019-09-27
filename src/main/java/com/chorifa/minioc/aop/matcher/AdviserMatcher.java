package com.chorifa.minioc.aop.matcher;

import com.chorifa.minioc.aop.Adviser;
import com.chorifa.minioc.aop.interceptor.MethodInterceptor;
import com.chorifa.minioc.utils.exceptions.AopException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdviserMatcher {

    public final static char DELIMITER = ':';

    @Deprecated
    private final List<Adviser> advisers = new ArrayList<>();

    private final List<MethodInterceptor> interceptors = new ArrayList<>();

    @Deprecated
    public void addAdviser(Adviser adviser){
        advisers.add(adviser);
    }

    @Deprecated
    public void addAdvisers(Adviser[] advisers){
        this.advisers.addAll(Arrays.asList(advisers));
    }

    public void addInterceptors(List<MethodInterceptor> interceptors){
        if(interceptors != null)
            this.interceptors.addAll(interceptors);
    }

    public void sortInterceptors(){
        Collections.sort(this.interceptors);
    }

    public List<MethodInterceptor> getInterceptorMatchForClass(Class<?> clazz){
        String className = clazz.getCanonicalName();
        String pattern;
        List<MethodInterceptor> list = new ArrayList<>();
        for(MethodInterceptor interceptor : interceptors){
            pattern = interceptor.getPattern();
            if(isMatch(className,pattern.substring(0,pattern.lastIndexOf(DELIMITER))))
                list.add(interceptor);
        }
        return list;
    }

    @Deprecated
    public List<Adviser> getAdviserMatchForClass(Class<?> clazz){
        String className = clazz.getCanonicalName();
        String pattern;
        List<Adviser> list = new ArrayList<>();
        for(Adviser adviser : advisers){
            pattern = adviser.getScope();
            if(isMatch(className,pattern.substring(0,pattern.lastIndexOf(DELIMITER))))
                list.add(adviser);
        }
        return list;
    }

    @Deprecated
    public Adviser[] getPriorityAdviserMatchForMethod(String methodName, List<Adviser> list){
        if(list == null || list.isEmpty()) return null;
        Adviser[] advisers = new Adviser[5];
        for(int i = 0; i < 5; i++)
            advisers[i] = null;
        String pattern; String[] strs;
        for(Adviser adviser : list){
            strs = adviser.getScope().split(String.valueOf(DELIMITER));
            if(strs.length < 2) throw new AopException("AdviserMatcher: Adviser pattern illegal -> "+adviser.getScope());
            pattern = strs[strs.length-1];
            if(pattern.equals("*") || isMatch(methodName,pattern)){
                if(advisers[adviser.getAdviceType().ordinal()] == null)
                    advisers[adviser.getAdviceType().ordinal()] = adviser;
                else throw new AopException("AdviserMatcher: multi-aop type used in one method "
                        +methodName+" -> "+adviser.getScope()+" with "+advisers[adviser.getAdviceType().ordinal()].getScope());
            }
        }
        for(int i = 0; i < 5; i++)
            if(advisers[i] != null)
                return advisers;
        return null;
    }

    /**
     * only support '*' (match 0 char, 1 char or more chars)
     * @param s origin string
     * @param p pattern string
     * @return p is match s
     */
    public static boolean isMatch(String s, String p){
        boolean[][] memo = new boolean[s.length()+1][p.length()+1];
        memo[0][0] = true;
        for(int i = 0; i < p.length(); i++){ // what if p start with *
            if(memo[0][i]){
                if(p.charAt(i) == '*') memo[0][i+1] = true;
                else break;
            }
        }
        for(int i = 0; i < s.length(); i++) {
            for (int j = 0; j < p.length(); j++) {
                if(s.charAt(i) == p.charAt(j))
                    memo[i+1][j+1] = memo[i][j];
                else if(p.charAt(j) == '*'){
                    // respectively, * match 0 char or 1++ chars
                    memo[i+1][j+1] = memo[i+1][j] | memo[i][j+1];
                }
            }
        }
        return memo[s.length()][p.length()];
    }

}
