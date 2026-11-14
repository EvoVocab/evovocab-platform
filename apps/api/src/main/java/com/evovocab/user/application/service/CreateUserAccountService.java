package com.evovocab.user.application.service;

import com.evovocab.user.application.port.in.CreateUserAccountCommand;
import com.evovocab.user.application.port.in.CreateUserAccountUseCase;
import com.evovocab.user.application.port.out.UserResult;
import com.evovocab.user.domain.model.Password;
import com.evovocab.user.domain.model.UserAccount;
import com.evovocab.user.domain.model.UserAccountId;
import com.evovocab.user.domain.model.UserAccountProvider;
import com.evovocab.user.domain.model.UserAccountStatus;
import com.evovocab.user.domain.model.UserProfile;
import com.evovocab.user.domain.repository.UserAccountRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserAccountService implements CreateUserAccountUseCase {

    private final UserAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResult execute(CreateUserAccountCommand command) {
        String email = command.email();
        UserAccountStatus accountStatus = command.status();
        UserAccountProvider accountProvider = command.provider();
        UserAccountId id = UserAccountId.generate();
        Instant now = Instant.now();
        Password password = Password.hashForPersistence(passwordEncoder.encode(command.password()));
        UserProfile profile = new UserProfile(id, command.fullName(), now, now);
        UserAccount account = new UserAccount(
            id,
            email,
            password,
            accountStatus,
            accountProvider,
            null,
            now,
            now,
            null,
            profile
        );
        UserAccount savedUserAccount = accountRepository.save(account);
        return new UserResult(
            savedUserAccount.getId().getValue(),
            savedUserAccount.getEmail(),
            savedUserAccount.getStatus().name(),
            savedUserAccount.getProvider().name()
        );
    }
}
