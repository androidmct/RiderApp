package com.sage.cabapp.twilio.chat;

public interface TaskCompletionListener<T, U> {

  void onSuccess(T t);

  void onError(U u);
}
