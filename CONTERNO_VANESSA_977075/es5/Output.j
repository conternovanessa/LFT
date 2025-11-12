.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 invokestatic Output/read()I
 istore 0
 invokestatic Output/read()I
 istore 1
 goto L1
L1:
 goto L2
L2:
 iload 0
 iload 1
 if_icmpne L3
 goto L4
L3:
 iload 0
 iload 1
 if_icmpgt L5
 goto L6
L5:
 iload 0
 iload 1
 isub 
 istore 0
 goto L8
L8:
 goto L7
L6:
 iload 1
 iload 0
 isub 
 istore 1
 goto L9
L9:
 goto L7
L7:
 goto L2
L4:
 iload 0
 invokestatic Output/print(I)V
 iload 1
 ldc 1
 iadd 
 ldc 100
 iadd 
 invokestatic Output/print(I)V
 ldc 2023
 invokestatic Output/print(I)V
 goto L10
L10:
 ldc 0
 istore 2
 iload 2
 istore 3
 iload 3
 istore 4
 goto L11
L11:
 iload 2
 invokestatic Output/print(I)V
 iload 3
 invokestatic Output/print(I)V
 iload 4
 invokestatic Output/print(I)V
 goto L12
L12:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

