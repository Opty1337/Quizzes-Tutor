package pt.ulisboa.tecnico.socialsoftware.tutor.tournament.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.TutorException;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.TournamentService;
import pt.ulisboa.tecnico.socialsoftware.tutor.tournament.dto.TournamentDto;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import java.security.Principal;

import static pt.ulisboa.tecnico.socialsoftware.tutor.exceptions.ErrorMessage.AUTHENTICATION_ERROR;

@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/tournaments")
    @PreAuthorize("hasRole('ROLE_STUDENT') and hasPermission(#tournamentDto, 'TOURNAMENT.CREATE')")
    public TournamentDto createTournament(Principal principal, @RequestBody TournamentDto tournamentDto) {
        User user = (User) ((Authentication) principal).getPrincipal();
        if(user == null) { throw new TutorException(AUTHENTICATION_ERROR); }

        tournamentDto.setCreatorId(user.getId());
        return tournamentService.createTournament(tournamentDto);
    }

    @PutMapping("/tournaments/{tournamentId}")
    @PreAuthorize("hasRole('ROLE_STUDENT') and hasPermission(#tournamentId, 'TOURNMENT.ACCESS')")
    public void enrollPlayer(Principal principal, @PathVariable int tournamentId) {
        User user = (User) ((Authentication) principal).getPrincipal();
        if(user == null) { throw new TutorException(AUTHENTICATION_ERROR); }

        tournamentService.enrollPlayer(user.getId(), tournamentId);
    }
}