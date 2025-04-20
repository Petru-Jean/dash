package org.dash.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "guild")
public class GuildEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


}
