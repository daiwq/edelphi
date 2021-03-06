package fi.metatavu.edelphi.rest.model;

import java.time.*;
import java.util.*;

import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


public class QueryPageLive2DAxis   {
  private @Valid String label = null;  private @Valid QueryPageLive2DColor color = null;  private @Valid List<String> options = new ArrayList<>();

  /**
   **/
  public QueryPageLive2DAxis label(String label) {
    this.label = label;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("label")
  public String getLabel() {
    return label;
  }
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   **/
  public QueryPageLive2DAxis color(QueryPageLive2DColor color) {
    this.color = color;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("color")
  public QueryPageLive2DColor getColor() {
    return color;
  }
  public void setColor(QueryPageLive2DColor color) {
    this.color = color;
  }

  /**
   **/
  public QueryPageLive2DAxis options(List<String> options) {
    this.options = options;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("options")
  public List<String> getOptions() {
    return options;
  }
  public void setOptions(List<String> options) {
    this.options = options;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryPageLive2DAxis queryPageLive2DAxis = (QueryPageLive2DAxis) o;
    return Objects.equals(label, queryPageLive2DAxis.label) &&
        Objects.equals(color, queryPageLive2DAxis.color) &&
        Objects.equals(options, queryPageLive2DAxis.options);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, color, options);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QueryPageLive2DAxis {\n");
    
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    options: ").append(toIndentedString(options)).append("\n");
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
