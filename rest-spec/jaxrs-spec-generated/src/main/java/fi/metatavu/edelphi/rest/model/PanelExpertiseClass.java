package fi.metatavu.edelphi.rest.model;

import java.time.*;
import java.util.*;

import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


public class PanelExpertiseClass   {
  private @Valid Long id = null;  private @Valid String name = null;

  /**
   * Id
   **/
  public PanelExpertiseClass id(Long id) {
    this.id = id;
    return this;
  }

  
  @ApiModelProperty(value = "Id")
  @JsonProperty("id")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Expertise class name
   **/
  public PanelExpertiseClass name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "Expertise class name")
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PanelExpertiseClass panelExpertiseClass = (PanelExpertiseClass) o;
    return Objects.equals(id, panelExpertiseClass.id) &&
        Objects.equals(name, panelExpertiseClass.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PanelExpertiseClass {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
