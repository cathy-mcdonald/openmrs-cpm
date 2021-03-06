package org.openmrs.module.conceptreview.web.service;

import org.dozer.Mapper;
import org.openmrs.module.conceptpropose.web.dto.ProposedConceptReviewDto;
import org.openmrs.module.conceptpropose.web.dto.ProposedConceptReviewPackageDto;
import org.openmrs.module.conceptpropose.web.dto.SubmissionDto;
import org.openmrs.module.conceptreview.ProposedConceptReview;
import org.openmrs.module.conceptreview.ProposedConceptReviewPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ConceptReviewMapperService {

	private Mapper dozerBeanMapper;

	@Autowired
	public ConceptReviewMapperService(@Qualifier("conceptreviewmapper") Mapper dozerBeanMapper) {
		this.dozerBeanMapper = dozerBeanMapper;
	}

	public ProposedConceptReviewPackage convertSubmissionDtoToProposedConceptReviewPackage(final SubmissionDto incomingProposal){
		return dozerBeanMapper.map(incomingProposal, ProposedConceptReviewPackage.class);
	}

	public ProposedConceptReviewPackageDto convertProposedConceptReviewPackageToProposedConceptReviewDto(ProposedConceptReviewPackage proposedConceptReviewPackage){
		return dozerBeanMapper.map(proposedConceptReviewPackage, ProposedConceptReviewPackageDto.class);
	}

	public ProposedConceptReviewPackage convertProposedConceptReviewDtoToProposedConceptReviewPackage(ProposedConceptReviewPackageDto dto) {
		return dozerBeanMapper.map(dto, ProposedConceptReviewPackage.class);
	}

	public ProposedConceptReviewDto createProposedConceptReviewDto(ProposedConceptReview proposedConceptReview) {
		return dozerBeanMapper.map(proposedConceptReview, ProposedConceptReviewDto.class);
	}
}
