package com.group.libraryapp.dto.book.request;

public class BookRequest {

  public BookRequest(String name) {
    this.name = name;
  }

  private String name;

  public String getName() {
    return name;
  }

}
