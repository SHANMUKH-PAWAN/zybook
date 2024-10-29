package edu.ncsu.zybook.mapper;

import edu.ncsu.zybook.DTO.ContentReadDTO;
import edu.ncsu.zybook.domain.model.ImageContent;
import edu.ncsu.zybook.domain.model.TextContent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface ContentReadDTOMapper {

    // Map TextContent to ContentReadDTO
    ContentReadDTO toDTO(TextContent content);

    // Map ImageContent to ContentReadDTO
    @Mapping(source = "data", target = "data", qualifiedByName = "byteArrayToBase64")
    ContentReadDTO toDTO(ImageContent content);

    // Define a custom mapping method for byte[] to Base64 String conversion
    @Named("byteArrayToBase64")
    default String byteArrayToBase64(byte[] data) {
        System.out.println("Data "+data);
        System.out.println("Encoddddd "+Base64.getEncoder().encodeToString( (byte[]) data));
        return data != null ? Base64.getEncoder().encodeToString(data) : null;
    }
}
