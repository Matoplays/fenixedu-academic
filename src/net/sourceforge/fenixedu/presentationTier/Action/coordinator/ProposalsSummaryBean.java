package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;

public class ProposalsSummaryBean extends SummaryBean {

	private final Map<ProposalStatusType, Integer> proposalsCount = new HashMap<ProposalStatusType, Integer>();

	public ProposalsSummaryBean(ExecutionDegree executionDegree, DegreeCurricularPlan degreeCurricularPlan) {
		super(executionDegree, degreeCurricularPlan);
		initProposals();
	}

	public void initProposals() {
		proposalsCount.clear();
		for (ProposalStatusType status : ProposalStatusType.values()) {
			proposalsCount.put(status, 0);
		}
		proposalsCount.put(ProposalStatusType.TOTAL, getExecutionDegree().getProposals().size());
		setProposalsCount();
	}

	private void incProposals(Proposal proposal) {
		ProposalStatusType status = proposal.getProposalStatus();
		Integer count = proposalsCount.get(status);
		proposalsCount.put(status, count + 1);
	}

	private void setProposalsCount() {
		for (Proposal proposal : getExecutionDegree().getProposals()) {
			incProposals(proposal);
		}
	}

	private Integer getProposalsCountByStatus(ProposalStatusType status) {
		return proposalsCount.get(status);
	}

	public Integer getTotalProposalsCount() {
		return getProposalsCountByStatus(ProposalStatusType.TOTAL);
	}

	public Integer getApprovedProposalsCount() {
		return getProposalsCountByStatus(ProposalStatusType.APPROVED);
	}

	public Integer getPublishedProposalsCount() {
		return getProposalsCountByStatus(ProposalStatusType.PUBLISHED);
	}

	public Integer getForApprovalProposalsCount() {
		// return getTotalProposalsCount() - (getApprovedProposalsCount() +
		// getPublishedProposalsCount());
		return getProposalsCountByStatus(ProposalStatusType.FOR_APPROVAL);
	}

	public ProposalStatusType getTotalProposalsKey() {
		return ProposalStatusType.TOTAL;
	}

	public ProposalStatusType getApprovedProposalsKey() {
		return ProposalStatusType.APPROVED;
	}

	public ProposalStatusType getPublishedProposalsKey() {
		return ProposalStatusType.PUBLISHED;
	}

	public ProposalStatusType getForApprovalProposalsKey() {
		return ProposalStatusType.FOR_APPROVAL;
	}
}
