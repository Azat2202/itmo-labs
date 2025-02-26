package se.ifmo.is_lab1.services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import se.ifmo.is_lab1.exceptions.ProposalAlreadyCreated;
import se.ifmo.is_lab1.exceptions.UserNotFoundException;
import se.ifmo.is_lab1.messages.adminPanel.AdminProposalResponse;
import se.ifmo.is_lab1.messages.authentication.UserResponse;
import se.ifmo.is_lab1.models.AdminProposal;
import se.ifmo.is_lab1.models.User;
import se.ifmo.is_lab1.models.enums.Role;
import se.ifmo.is_lab1.repositories.AdminProposalRepository;
import se.ifmo.is_lab1.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminProposalService {
    private final AdminProposalRepository adminProposalRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public AdminProposalResponse createAdminProposal() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (adminProposalRepository.existsByUserId(user.getId())){
            throw new ProposalAlreadyCreated();
        }
        AdminProposal adminProposal = new AdminProposal();
        adminProposal.setUser(user);
        return modelMapper.map(adminProposalRepository.save(adminProposal), AdminProposalResponse.class);
    }

    public List<AdminProposalResponse> getAllAdminProposals() {
        List<AdminProposal> adminProposals = adminProposalRepository.findAll();
        return modelMapper.map(adminProposals, new TypeToken<List<AdminProposalResponse>>() {}.getType());
    }

    public UserResponse approveAdmin(Long proposalId) {
        AdminProposal adminProposal = adminProposalRepository
                .findById(proposalId)
                .orElseThrow(UserNotFoundException::new);
        User user = userRepository.findById(adminProposal.getUser().getId())
                .orElseThrow(UserNotFoundException::new);
        user.setRole(Role.ADMIN);
        UserResponse userResponse =
                modelMapper.map(userRepository.save(user), UserResponse.class);
        adminProposalRepository.deleteById(proposalId);
        return userResponse;
    }

    public void declineAdmin(Long proposalId){
        adminProposalRepository.deleteById(proposalId);
    }

}
