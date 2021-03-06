package org.openmrs.module.conceptreview.web.controller;

import org.openmrs.Concept;
import org.openmrs.ConceptSearchResult;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.conceptpropose.web.dto.ProposedConceptReviewDto;
import org.openmrs.module.conceptpropose.web.dto.concept.ConceptDto;
import org.openmrs.module.conceptpropose.web.dto.concept.SearchConceptResultDto;

import org.openmrs.module.conceptpropose.web.dto.factory.DescriptionDtoFactory;
import org.openmrs.module.conceptpropose.web.dto.factory.NameDtoFactory;
import org.openmrs.module.conceptreview.web.service.ConceptReviewMapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.openmrs.module.conceptreview.ProposedConceptReview;
import org.openmrs.module.conceptreview.ProposedConceptReviewPackage;
import org.openmrs.module.conceptpropose.web.dto.ProposedConceptReviewPackageDto;
import org.openmrs.module.conceptreview.api.ProposedConceptReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ReviewController {

	@Autowired
	private ConceptReviewMapperService mapperService;

	private final NameDtoFactory nameDtoFactory;
    private final DescriptionDtoFactory descriptionDtoFactory;

    @Autowired
    public ReviewController (
            final DescriptionDtoFactory descriptionDtoFactory,
            final NameDtoFactory nameDtoFactory) {
        this.descriptionDtoFactory = descriptionDtoFactory;
        this.nameDtoFactory = nameDtoFactory;
    }
	//
	// Pages
	//

	@RequestMapping(value = "module/conceptreview/proposalReview.list", method = RequestMethod.GET)
	public String listProposalReview() {
		return "/module/conceptreview/proposalReview";
	}

	//
	// REST endpoints
	//

	@RequestMapping(value = "/conceptreview/proposalReviews", method = RequestMethod.GET)
	public @ResponseBody ArrayList<ProposedConceptReviewPackageDto> getProposalReviews() {

		final List<ProposedConceptReviewPackage> allConceptProposalReviewPackages = Context.getService(ProposedConceptReviewService.class).getAllProposedConceptReviewPackages();
		final ArrayList<ProposedConceptReviewPackageDto> response = new ArrayList<ProposedConceptReviewPackageDto>();

		for (final ProposedConceptReviewPackage conceptProposalReviewPackage : allConceptProposalReviewPackages) {
			final ProposedConceptReviewPackageDto conceptProposalReviewPackageDto = mapperService.convertProposedConceptReviewPackageToProposedConceptReviewDto(conceptProposalReviewPackage);

			response.add(conceptProposalReviewPackageDto);
		}

		return response;
	}

	@RequestMapping(value = "/conceptreview/proposalReviews/{proposalId}", method = RequestMethod.GET)
	public @ResponseBody ProposedConceptReviewPackageDto getProposalReview(@PathVariable int proposalId) {
		final ProposedConceptReviewPackage entity = Context.getService(ProposedConceptReviewService.class).getProposedConceptReviewPackageById(proposalId);
		return mapperService.convertProposedConceptReviewPackageToProposedConceptReviewDto(entity);
	}

	@RequestMapping(value = "/conceptreview/proposalReviews/{proposalId}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteProposalReview(@PathVariable int proposalId) {
		Context.getService(ProposedConceptReviewService.class).deleteProposedConceptReviewPackageById(proposalId);
	}

	@RequestMapping(value = "/conceptreview/proposalReviews/{proposalId}/concepts/{conceptId}", method = RequestMethod.GET)
	public @ResponseBody
	ProposedConceptReviewDto getConceptReview(@PathVariable int proposalId, @PathVariable int conceptId) {
		final ProposedConceptReviewService service = Context.getService(ProposedConceptReviewService.class);
		final ProposedConceptReview proposedConcept = service.getProposedConceptReviewPackageById(proposalId).getProposedConcept(conceptId);
		return mapperService.createProposedConceptReviewDto(proposedConcept);
	}

	@RequestMapping(value = "/conceptreview/proposalReviews/{proposalId}/concepts/{conceptId}", method = RequestMethod.PUT)
	public @ResponseBody
	ProposedConceptReviewDto updateConceptReview(@PathVariable int proposalId, @PathVariable int conceptId, @RequestBody ProposedConceptReviewDto updatedProposalReview) {
		final ProposedConceptReviewService service = Context.getService(ProposedConceptReviewService.class);
		final ProposedConceptReviewPackage aPackage = service.getProposedConceptReviewPackageById(proposalId);
		final ProposedConceptReview proposedConcept = aPackage.getProposedConcept(conceptId);
		if (proposedConcept != null) {
			proposedConcept.setReviewComment(updatedProposalReview.getReviewComment());
			proposedConcept.setStatus(updatedProposalReview.getStatus());

			if (updatedProposalReview.getConceptId() != 0) {
				proposedConcept.setConcept(Context.getConceptService().getConcept(updatedProposalReview.getConceptId()));
			}

			service.saveProposedConceptReviewPackage(aPackage);
		}
		return mapperService.createProposedConceptReviewDto(proposedConcept);
	}

    @RequestMapping(value = "/conceptreview/concepts", method = RequestMethod.GET)
    public @ResponseBody SearchConceptResultDto findConcepts(@RequestParam final String query,
                                                             @RequestParam final String requestNum) {
        final ArrayList<ConceptDto> results = new ArrayList<ConceptDto>();
        final ConceptService conceptService = Context.getConceptService();

        if (query.equals("")) {
            final List<Concept> allConcepts = conceptService.getAllConcepts("name", true, false);
            for (final Concept concept : allConcepts) {
                ConceptDto conceptDto = createConceptDto(concept);
                results.add(conceptDto);
            }
        } else {
            final List<ConceptSearchResult> concepts = conceptService.getConcepts(query, Context.getLocale(), false);
            for (final ConceptSearchResult conceptSearchResult : concepts) {
                ConceptDto conceptDto = createConceptDto(conceptSearchResult.getConcept());
                results.add(conceptDto);

            }

        }
        SearchConceptResultDto resultDto = new SearchConceptResultDto();
        resultDto.setConcepts(results);
        resultDto.setRequestNum(requestNum);
        return resultDto;
    }
    private ConceptDto createConceptDto(final Concept concept) {

        final ConceptDto dto = new ConceptDto();
        dto.setId(concept.getConceptId());
// TODO: fix this. was getting import errors
        dto.setNames(nameDtoFactory.create(concept));
        dto.setPreferredName(concept.getName().getName());
        dto.setDatatype(concept.getDatatype().getName());
// TODO: fix this. was getting import errors
        dto.setDescriptions(descriptionDtoFactory.create(concept));
        if(concept.getDescription()!=null)  {
            dto.setCurrLocaleDescription(concept.getDescription().getDescription());
        }

        return dto;
    }

}
