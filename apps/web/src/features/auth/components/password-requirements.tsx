import * as React from 'react';
import { ResponsiveBar, type BarDatum } from '@nivo/bar';
import { Check, X } from 'lucide-react';

import { PASSWORD_RULES } from '../constants/password-rules';
import { cn, SegmentBar, useDebounce } from '@evovocab/ui';

const DEBOUNCE_MS = 200;
const MEDIUM_STRENGTH_THRESHOLD = 3;
const CHART_MARGIN = { top: 0, right: 0, bottom: 0, left: 0 };

type RuleResult = (typeof PASSWORD_RULES)[number] & { met: boolean };

function getStrengthFillClass(metCount: number, total: number) {
  if (metCount === total) return 'fill-green-500';
  if (metCount >= MEDIUM_STRENGTH_THRESHOLD) return 'fill-yellow-500';
  return 'fill-destructive';
}

// SegmentBar đọc `${id}` (độ rộng mỗi đoạn) và `${id}_met` (đoạn đã đạt hay chưa)
function buildChartData(results: Array<RuleResult>, fillClass: string): Array<BarDatum> {
  const datum: BarDatum = { strength: 'password', fillClass };

  for (const rule of results) {
    datum[rule.id] = 1;
    datum[`${rule.id}_met`] = rule.met ? 1 : 0;
  }

  return [datum];
}

interface StrengthBarProps {
  results: Array<RuleResult>;
  fillClass: string;
}

function StrengthBar({ results, fillClass }: StrengthBarProps) {
  return (
    <div aria-hidden className="h-2 w-full">
      <ResponsiveBar
        animate={false}
        axisBottom={null}
        axisLeft={null}
        axisRight={null}
        axisTop={null}
        barComponent={SegmentBar}
        data={buildChartData(results, fillClass)}
        enableGridX={false}
        enableGridY={false}
        enableLabel={false}
        groupMode="stacked"
        indexBy="strength"
        innerPadding={3}
        isInteractive={false}
        keys={results.map((rule) => rule.id)}
        layout="horizontal"
        margin={CHART_MARGIN}
        padding={0}
        valueScale={{ type: 'linear', max: results.length, min: 0 }}
      />
    </div>
  );
}

interface RequirementItemProps {
  label: string;
  met: boolean;
}

function RequirementItem({ label, met }: RequirementItemProps) {
  const Icon = met ? Check : X;

  return (
    <li
      className={cn(
        'flex items-center gap-2 text-xs transition-colors',
        met ? 'text-green-600 dark:text-green-500' : 'text-muted-foreground',
      )}
    >
      <Icon className="size-3.5 shrink-0" />
      <span>{label}</span>
    </li>
  );
}

interface PasswordRequirementsProps {
  password: string;
}

function PasswordRequirements({ password }: PasswordRequirementsProps) {
  const debouncedPassword = useDebounce<string>(password, DEBOUNCE_MS);

  const results = React.useMemo<Array<RuleResult>>(
    () => PASSWORD_RULES.map((rule) => ({ ...rule, met: rule.test(debouncedPassword) })),
    [debouncedPassword],
  );

  const total = results.length;
  const metCount = results.filter((rule) => rule.met).length;
  const fillClass = getStrengthFillClass(metCount, total);

  return (
    <div aria-live="polite" className="space-y-2">
      <div className="flex items-center justify-between text-xs">
        <span className="text-muted-foreground">Yêu cầu mật khẩu</span>
        <span className="font-medium">
          {metCount}/{total}
        </span>
      </div>

      <StrengthBar fillClass={fillClass} results={results} />

      <ul className="space-y-1">
        {results.map((rule) => (
          <RequirementItem key={rule.id} label={rule.label} met={rule.met} />
        ))}
      </ul>
    </div>
  );
}

export { PasswordRequirements };
