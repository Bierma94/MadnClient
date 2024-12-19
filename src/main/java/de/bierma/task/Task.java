package de.bierma.task;

import java.net.URISyntaxException;
import java.net.http.HttpRequest;

/**
 * Task
 * Copyright (c) Jannes Bierma -All Rights Reserved.
 *
 * @author Jannes Bierma (jannes.bierma@stud.hs-emden-leer.de)
 * @version 1.0 - 17.12.2024
 */
public interface Task {
    HttpRequest call() throws URISyntaxException;

}
