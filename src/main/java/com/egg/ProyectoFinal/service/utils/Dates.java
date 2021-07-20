
package com.egg.ProyectoFinal.service.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Dates {
    
    private static final String[] monthNames = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };
    
    // PRIVATE CONSTRUCTOR
    
    private Dates(){}
    
    // MÉTODOS
    
    public static String getCurrentDateAsString(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public static int getCurrentYear(){
        return YearMonth.now().getYear();
    }
    
    public static int getCurrentMonth(){
        return YearMonth.now().getMonthValue();
    }
    
    public static String getCurrentMonthName(){
        return monthNames[getCurrentMonth() - 1];
    }
    
    public static String getMonthName(int mes){
        return monthNames[mes - 1];
    }
    
    public static Date getFirstOfCurrentMonth(){
        return java.sql.Date.valueOf(YearMonth.now().atDay(1));
    }
    
    public static Date getLastOfCurrentMonth(){
        return java.sql.Date.valueOf(YearMonth.now().atEndOfMonth());
    }
    
    public static Date getFirstOfMonth(int anio, int mes){
        return java.sql.Date.valueOf(YearMonth.of(anio, mes).atDay(1));
    }
    
    public static Date getLastOfMonth(int anio, int mes){
        return java.sql.Date.valueOf(YearMonth.of(anio, mes).atEndOfMonth());
    }
    
    public static Integer daysInMonth(Date fecha){
        Calendar date = Calendar.getInstance();
        date.setTime(fecha);

        YearMonth yearMonth = YearMonth.of(date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1);
        return yearMonth.lengthOfMonth();
    }
    
    public static Date getDatePlusDays(Date startingDate, int periodicidad){
        Calendar date = new GregorianCalendar();
        date.setTime(startingDate);
        
        int anio = date.get(Calendar.YEAR);
        int mes = date.get(Calendar.MONTH) + 1;
        int dia = date.get(Calendar.DAY_OF_MONTH);
        
        LocalDate fecha = LocalDate.of(anio, mes, dia);
        
        return java.sql.Date.valueOf(fecha.plusDays(periodicidad));
    }
    
}
