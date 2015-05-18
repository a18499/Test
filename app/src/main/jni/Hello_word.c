#include <com_example_peter_test_MainActivity.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

  JNIEXPORT jstring JNICALL Java_com_example_peter_test_MainActivity_getStringFromNative
  (JNIEnv * evn, jobject obj){
    int a = 3;
    int b = 5;
    int fd2;
    int fd;
    char s[] = "Linux Programmer!\n";
    fd2 = open("/data/local/tmp/test_open_file2.txt", O_WRONLY|O_CREAT);
    if(fd2>0){
        write(fd2, s, sizeof(s));
        close(fd2);
    }
    else{
        printf("file create failed...\n"");
    }
    fd = open("/data/local/tmp/test_open_file.txt", O_RDONLY);
    if (fd>0) {
        close(fd);
        return (*evn)->NewStringUTF(evn,"file open success...\n");
    }
    else {
         return (*evn)->NewStringUTF(evn,"file open failed...\n");
    }

  }
  JNIEXPORT jint JNICALL Java_com_example_peter_test_MainActivity_add
    (JNIEnv * evn, jobject odj, jint a, jint b){
     int anser = a+b;
     printf("anser = %d",anser);


     return anser;

    }