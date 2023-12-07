package kr.nadeuli.mapper;

import kr.nadeuli.dto.*;
import kr.nadeuli.entity.*;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;
@Mapper(builder = @Builder(disableBuilder = true), componentModel = "spring")
public interface OriScheMemChatFavMapper {

    @Mapping(source = "member", target = "member", qualifiedByName = "memberDTOToMember")
    @Mapping(source = "orikkiri", target = "orikkiri", qualifiedByName = "orikkiriDTOToOrikkiri")
    @Mapping(source = "orikkiriSchedule", target = "orikkiriSchedule", qualifiedByName = "orikkiriScheduleDTOToOrikkiriSchedule")
    @Mapping(source = "product", target = "product", qualifiedByName = "productDTOToProduct")
//    @Mapping(source = "ansQuestions", target = "ansQuestions", qualifiedByName = "ansQuestionDTOListToAnsQuestionList")
    OriScheMemChatFav oriScheMemChatFavDTOToOriScheMemChatFav(OriScheMemChatFavDTO oriScheMemChatFavDTO);

    @Mapping(source = "member", target = "member", qualifiedByName = "memberToMemberDTO")
    @Mapping(source = "orikkiri", target = "orikkiri", qualifiedByName = "orikkiriToOrikkiriDTO")
    @Mapping(source = "orikkiriSchedule", target = "orikkiriSchedule", qualifiedByName = "orikkiriScheduleToOrikkiriScheduleDTO")
    @Mapping(source = "product", target = "product", qualifiedByName = "productToProductDTO")
//    @Mapping(source = "ansQuestions", target = "ansQuestions", qualifiedByName = "ansQuestionListToAnsQuestionDTOList")
    OriScheMemChatFavDTO oriScheMemChatFavToOriScheMemChatFavDTO(OriScheMemChatFav oriScheMemChatFav);

    @Named("memberDTOToMember")
    default Member memberDTOToMember(MemberDTO memberDTO){
        if(memberDTO == null){
            return null;
        }
        return Member.builder().tag(memberDTO.getTag()).build();
    }

    @Named("memberToMemberDTO")
    default MemberDTO memberToMemberDTO(Member member){
        if(member == null){
            return null;
        }
        return MemberDTO.builder().tag(member.getTag()).build();
    }

    @Named("orikkiriDTOToOrikkiri")
    default Orikkiri orikkiriDTOToOrikkiri(OrikkiriDTO orikkiriDTO){
        if(orikkiriDTO == null){
            return null;
        }
        return Orikkiri.builder().orikkiriId(orikkiriDTO.getOrikkiriId()).build();
    }

    @Named("orikkiriToOrikkiriDTO")
    default OrikkiriDTO orikkiriToOrikkiriDTO(Orikkiri orikkiri){
        if(orikkiri == null){
            return null;
        }
        return OrikkiriDTO.builder().orikkiriId(orikkiri.getOrikkiriId()).build();
    }

    @Named("orikkiriScheduleDTOToOrikkiriSchedule")
    default OrikkiriSchedule orikkiriScheduleDTOToOrikkiriSchedule(OrikkiriScheduleDTO orikkiriScheduleDTO){
        if(orikkiriScheduleDTO == null){
            return null;
        }
        return OrikkiriSchedule.builder().orikkiriScheduleId(orikkiriScheduleDTO.getOrikkiriScheduleId()).build();
    }

    @Named("orikkiriScheduleToOrikkiriScheduleDTO")
    default OrikkiriScheduleDTO orikkiriScheduleToOrikkiriScheduleDTO(OrikkiriSchedule orikkiriSchedule){
        if(orikkiriSchedule == null){
            return null;
        }
        return OrikkiriScheduleDTO.builder().orikkiriScheduleId(orikkiriSchedule.getOrikkiriScheduleId()).build();
    }

    @Named("productDTOToProduct")
    default Product productDTOToProduct(ProductDTO productDTO){
        if(productDTO == null){
            return null;
        }
        return Product.builder().productId(productDTO.getProductId()).build();
    }

    @Named("productToProductDTO")
    default ProductDTO productToProductDTO(Product product){
        if(product == null){
            return null;
        }
        return ProductDTO.builder().productId(product.getProductId()).build();
    }

//    // 추가된 메서드: DTO에서 Entity로 List<AnsQuestion> 매핑하는 메서드
//    @Named("ansQuestionsDTOListToAnsQuestionsList") // 이름 수정
//    default List<AnsQuestion> ansQuestionsDTOListToAnsQuestionsList(List<AnsQuestionDTO> ansQuestionDTO) {
//        if (ansQuestionDTO == null) {
//            return null;
//        }
//        return ansQuestionDTO.stream()
//                .map(AnsQuestionDTO -> AnsQuestion.builder().content(ansQuestionDTO.getContent()).build())
//                .collect(Collectors.toList());
//    }
//
//    // 추가된 메서드: Entity에서 DTO로 List<AnsQuestion> 매핑하는 메서드
//    @Named("ansQuestionListToAnsQuestionDTOList")
//    default List<AnsQuestionDTO> ansQuestionListToAnsQuestionDTOList(List<AnsQuestion> ansQuestions) {
//        if (ansQuestions == null) {
//            return null;
//        }
//        return ansQuestions.stream()
//                .map(AnsQuestions-> AnsQuestionDTO.builder().content(ansQuestions.getContent()).build())
//                .collect(Collectors.toList());
//    }
}

