package com.moneyApp.payment;

import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

class Payment
{
    static Payment restore(PaymentSnapshot snapshot)
    {
        return new Payment(
                snapshot.getId()
                , snapshot.getStartDate()
                , snapshot.getFrequencyType()
                , snapshot.getFrequency()
                , snapshot.getDescription()
                , snapshot.getAmount()
                , snapshot.getUser()
                , snapshot.getPositions().stream().map(Position::restore).collect(Collectors.toSet())
        );
    }

    private final Long id;
    private final LocalDate startDate;
    private final PaymentFrequency frequencyType;
    private final int frequency;
    private final String description;
    private final double amount;
    private final UserSource user;
    private final Set<Position> positions;

    private Payment(
            final Long id
            , final LocalDate startDate
            , final PaymentFrequency frequencyType
            , final int frequency
            , final String description
            , final double amount
            , final UserSource user
            , final Set<Position> positions)
    {
        this.id = id;
        this.startDate = startDate;
        this.frequencyType = frequencyType;
        this.frequency = frequency;
        this.description = description;
        this.amount = amount;
        this.user = user;
        this.positions = positions;
    }

    PaymentSnapshot getSnapshot()
    {
        return new PaymentSnapshot(
                this.id
                , this.startDate
                , this.frequencyType
                , this.frequency
                , this.description
                , this.amount
                , this.user
                , this.positions.stream().map(Position::getSnapshot).collect(Collectors.toSet())
        );
    }

    static class Position
    {
        static Position restore(PaymentPositionSnapshot snapshot)
        {
            return new Position(
                    snapshot.getId()
                    , snapshot.getPaymentDate()
                    , snapshot.isPaid()
                    , snapshot.getHash()
            );
        }

        private final Long id;
        private final LocalDate paymentDate;
        private final boolean isPaid;
        private final int hash;

        private Position(final Long id, final LocalDate paymentDate, final boolean isPaid, final int hash)
        {
            this.id = id;
            this.paymentDate = paymentDate;
            this.isPaid = isPaid;
            this.hash = hash;
        }

        public Position(final long id, final LocalDate paymentDate)
        {
            this.id = id;
            this.paymentDate = paymentDate;
//        false by default because it's assumed that payment will be done in future
            this.isPaid = false;
//            hash made from payment date and three digits random int
            this.hash = hashCode(generateRandom());
        }

        PaymentPositionSnapshot getSnapshot()
        {
            return new PaymentPositionSnapshot(
                    this.id
                    , this.paymentDate
                    , this.isPaid
                    , this.hash
            );
        }

        private int generateRandom()
        {
            var generator = new Random();

            return generator.nextInt(99, 1000);
        }

        int hashCode(int random)
        {
            return Objects.hash(this.paymentDate, random);
        }

        public Long getId()
        {
            return this.id;
        }

        public LocalDate getPaymentDate()
        {
            return this.paymentDate;
        }

        public boolean isPaid()
        {
            return this.isPaid;
        }

        public int getHash()
        {
            return this.hash;
        }
    }
}
