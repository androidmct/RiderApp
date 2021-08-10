package com.sage.cabapp.data;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
@Entity
public class Person {
    @Id
    public long id;
    public String name;
}
