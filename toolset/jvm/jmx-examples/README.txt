
# ==============================================================================
#
#  JMX Tutorial Introductory Example : Instrumenting Your Own Applications.
#
#  The aim of this introductory example is to show the basic features of
#  the JMX technology first by instrumenting a simple resource and second
#  by performing operations on it using the jconsole tool. This example
#  shows the implementation of a standard MBean, how to register it in the
#  platform's MBean Server and how to perform remote operations on it by
#  connecting to the RMI connector server using the jconsole tool. Besides
#  monitoring the application, jconsole will also allow you to observe the
#  built-in JVM instrumentation as the JVM's MBeans are also registered in
#  the platform's MBean Server. This examples also shows how the existing
#  platform's MBean Server can be shared between the JVM and the application
#  itself to register the application MBeans, thus avoiding the creation of
#  multiple MBean Server instances on the same JVM.
#
# ==============================================================================
#
# In order to compile and run the example, make a copy of this README file, and
# then simply cut and paste all the commands as needed into a terminal window.
#
# This README makes the assumption that you are running under Java SE 6 on Unix,
# you are familiar with the JMX technology, and with the bourne shell or korn
# shell syntax.
#
# All the commands below are defined using Unix korn shell syntax.
#
# If you are not running Unix and korn shell you are expected to be able to
# adapt these commands to your favorite OS and shell environment.
#

# Compile Java classes
#
# The Java classes used in this example are contained in the com.example.mbeans
# Java package.
#
# * Main.java: gets the Platform MBean Server, and creates
#              and registers the Hello World MBean on it.
#
# * Hello.java: implements the Hello World standard MBean.
#
# * HelloMBean.java: the management interface exposed by
#                    the Hello World standard MBean.
#

javac com/example/mbeans/*.java

# Start the Main application
#

java com.example.mbeans.Main

# Start jconsole on a different shell window on the same machine
#
# JConsole is located in $(J2SE_HOME)/bin/jconsole
#

jconsole

# ==============================================================================






/* Main.java - main class for Hello World example.  Create the
   HelloWorld MBean, register it, then wait forever (or until the
   program is interrupted).  */

package com.example.mbeans;

import java.lang.management.*;
import javax.management.*;

public class Main {
    /* For simplicity, we declare "throws Exception".  Real programs
       will usually want finer-grained exception handling.  */
    public static void main(String[] args) throws Exception {
        // Get the Platform MBean Server
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        // Construct the ObjectName for the MBean we will register
        ObjectName name = new ObjectName("com.example.mbeans:type=Hello");

        // Create the Hello World MBean
        Hello mbean = new Hello();

        // Register the Hello World MBean
        mbs.registerMBean(mbean, name);

        // Wait forever
        System.out.println("Waiting forever...");
        Thread.sleep(Long.MAX_VALUE);
    }
}




/* Hello.java - MBean implementation for the Hello World MBean.
   This class must implement all the Java methods declared in the
   HelloMBean interface, with the appropriate behavior for each one.  */

package com.example.mbeans;

public class Hello implements HelloMBean {
    public void sayHello() {
        System.out.println("hello, world");
    }

    public int add(int x, int y) {
        return x + y;
    }

    /* Getter for the Name attribute.  The pattern shown here is
       frequent: the getter returns a private field representing the
       attribute value.  In our case, the attribute value never
       changes, but for other attributes it might change as the
       application runs.  Consider an attribute representing
       statistics such as uptime or memory usage, for example.  Being
       read-only just means that it can't be changed through the
       management interface.  */
    public String getName() {
        return this.name;
    }

    /* Getter for the CacheSize attribute.  The pattern shown here is
       frequent: the getter returns a private field representing the
       attribute value, and the setter changes that field.  */
    public int getCacheSize() {
        return this.cacheSize;
    }

    /* Setter for the CacheSize attribute.  To avoid problems with
       stale values in multithreaded situations, it is a good idea
       for setters to be synchronized.  */
    public synchronized void setCacheSize(int size) {
        this.cacheSize = size;

        /* In a real application, changing the attribute would
           typically have effects beyond just modifying the cacheSize
           field.  For example, resizing the cache might mean
           discarding entries or allocating new ones.  The logic for
           these effects would be here.  */
        System.out.println("Cache size now " + this.cacheSize);
    }

    private final String name = "Reginald";
    private int cacheSize = DEFAULT_CACHE_SIZE;
    private static final int DEFAULT_CACHE_SIZE = 200;
}





/* HelloMBean.java - MBean interface describing the management
   operations and attributes for the Hello World MBean.  In this case
   there are two operations, "sayHello" and "add", and two attributes,
   "Name" and "CacheSize". */

package com.example.mbeans;

public interface HelloMBean {
    // operations

    public void sayHello();
    public int add(int x, int y);

    // attributes

    // a read-only attribute called Name of type String
    public String getName();

    // a read-write attribute called CacheSize of type int
    public int getCacheSize();
    public void setCacheSize(int size);
}
