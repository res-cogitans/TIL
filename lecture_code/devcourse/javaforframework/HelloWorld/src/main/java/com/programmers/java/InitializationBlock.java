package com.programmers.java;

class BlockData {
    static int classVar = 1;    // 클래스 명시적 초기화
    int instanceVar = 2;        // 인스턴스 명시적 초기화
    static {
        classVar = 3;           // 클래스 초기화 블록
        System.out.println("클래스 초기화 블록");
    }
    {
        instanceVar = 4;         // 인스턴스 초기화 블록
        System.out.println("인스턴스 초기화 블록");
    }

    public BlockData() {
        classVar = 5;           // 생성자 초기화
        instanceVar = 6;
        System.out.println("생성자 실행");
    }
}

public class InitializationBlock {

    public static void main(String[] args) {
        System.out.println("---------------------------------------");

        System.out.println("BlockData.classVar = " + BlockData.classVar);

        BlockData blockData = new BlockData();

        System.out.println("인스턴스 생성");

        System.out.println("BlockData.classVar = " + BlockData.classVar);
        System.out.println("blockData.instanceVar = " + blockData.instanceVar);

        System.out.println("---------------------------------------");


    }
}
