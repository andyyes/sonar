/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2013 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.ws;

import org.sonar.api.server.ws.Request;

import javax.servlet.http.HttpServletRequest;

public class ServletRequest extends Request {

  private final HttpServletRequest source;
  private String mediaType = "application/json";

  public ServletRequest(HttpServletRequest source) {
    this.source = source;
  }

  @Override
  public String param(String key) {
    return source.getParameter(key);
  }

  @Override
  public String mediaType() {
    return mediaType;
  }

  public ServletRequest setMediaType(String s) {
    this.mediaType = s;
    return this;
  }

  @Override
  public boolean isPost() {
    return "POST".equals(source.getMethod());
  }
}
