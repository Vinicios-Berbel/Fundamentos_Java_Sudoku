package br.com.vinicios.service;

import br.com.vinicios.model.EventEnum;

public interface EventListener {

    void update(final EventEnum eventType);
}
