package backend.codebackend.service;

import backend.codebackend.domain.Account;
import backend.codebackend.domain.Member;
import backend.codebackend.domain.PaymentDetails;
import backend.codebackend.domain.SystemAccount;
import backend.codebackend.dto.AccountDto;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.repository.PaymentDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
public class PaymentsDetailsService {
    private final AccountService accountService;
    private final SystemAccountService systemAccountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberService memberService;
    private final PaymentDetailsRepository paymentDetailsRepository;

    //각 사용자의 거래내역 조회
    public List<PaymentDetails> findAll(Long id) {
        return paymentDetailsRepository.findAll(id);
    }

    // 송금 처리 (사용자 -> 시스템)
    public boolean sendMoneyToSystemAccount(String nickname, int price) {
        // 송금자 정보 가져오기
        Optional<Member> member = memberService.findByName(nickname);

        if(member.isEmpty())
            return false;

        Account senderAccount = accountService.findAccount(member.get().getId());
        AccountDto senderAccountDto = AccountDto.builder()
                .id(senderAccount.getId())
                .number(senderAccount.getNumber())
                .password(bCryptPasswordEncoder.encode(senderAccount.getPassword())) //비밀번호 암호화
                .username(senderAccount.getUsername())
                .balance(senderAccount.getBalance())
                .account_name(senderAccount.getAccount_name())
                .build();

        // 시스템 계좌 정보 가져오기
        SystemAccount systemAccount = systemAccountService.getSystemAccountInfo();

        // 송금자의 계좌에서 돈 감소
        if (senderAccount.getBalance() >= price){
            senderAccount.setBalance(senderAccount.getBalance() - price);
            // 결제와 결제 완료 여부를 업데이트
            senderAccount.setIs_paid(1);
            if (senderAccount.getComplete_payment() == 1) {
                // 모든 사용자가 결제완료 버튼 누르면 돈을 송금
                accountService.save(senderAccountDto);

                // 시스템 계좌에 돈 추가
                systemAccount.setBalance(systemAccount.getBalance() + price);
                systemAccountService.updateSystemAccount(systemAccount);

                return true;
            }
        }
        return false;
    }


}
