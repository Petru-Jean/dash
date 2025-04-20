package org.dash.repository;

import org.dash.entity.MessageCreateEventEntity;
import org.dash.event.incoming.MessageCreateEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<MessageCreateEventEntity, Integer>
{

}
